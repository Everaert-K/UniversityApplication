package com.systeemontwerp.cursusservice.domain;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.cursusservice.adapteres.messaging.PaymentMethod;
import com.systeemontwerp.cursusservice.persistence.LiteratureItemRepository;
import com.systeemontwerp.cursusservice.persistence.ReservationRepository;

import io.netty.handler.timeout.TimeoutException;

@Service
public class CursusService {
	
	private final Logger logger = Logger.getLogger(CursusService.class);
	private final LiteratureItemRepository literatureItemRepository;
	private final ReservationRepository reservationRepository;
	
	//Keep record of how much the school owes if a payment fails
	private final HashMap<String,Double> debt;
	
	private final BuyItemSaga buyItemSaga;
	
	@Autowired
	private CursusService(LiteratureItemRepository literatureItemRepository, ReservationRepository reservationRepository, BuyItemSaga buyItemSaga) {
		this.literatureItemRepository = literatureItemRepository;
		this.reservationRepository = reservationRepository;
		this.buyItemSaga = buyItemSaga;
		this.debt = new HashMap<>();
	}
	//Used for uploading new courses from profs
	public LiteratureItem uploadCursus(String title, String author, String profId, double price, byte[] data) throws CursusServiceException{
		if(!this.isValidProfId(profId))
			throw new CursusServiceException("Unable to verify profid");
		
		if(this.literatureItemRepository.findByTitleAndAuthor(title, author).orElse(new LiteratureItem()).getId() != null)
			throw new CursusServiceException("That cursus already exists");
		
		//Make the cursusobject and store it to the database
		LiteratureItem item = new LiteratureItem(title, author, price, profId, 0, false, data);
		
		//Request the studentservice for the amount of students -> max 2 seconds and default 10 books
		Long amount = 10 + this.requestStudentCount();
		item.setAmountAvailable(amount.intValue());
		
		this.literatureItemRepository.save(item);
		return item;
	}
	//Used for refilling stock aka printing
	public LiteratureItem restockItem(String literatureId, int amount) throws CursusServiceException {
		//Test if item exists
		if(!isValidLiteratureId(literatureId))
			throw new CursusServiceException("Item" + literatureId +" does not exist");
		
		//Restock the item
		LiteratureItem item = this.literatureItemRepository.findById(literatureId).get();
		item.setAmountAvailable(item.getAmountAvailable() + amount);
		
		//if the item is a book, we need to pay the bookstore, otherwise just print the courses
		if(item.isBook())
			this.buyItemSaga.startBuyBookSaga(item, amount);
		
		//Reserve the item for the people who were still waiting
		while(item.getReservees().size() > 0 && item.getAmountAvailable() > 0) {
			String reserveid = item.getReservees().get(0);
			item.setAmountAvailable(item.getAmountAvailable() - 1);
			List<LiteratureItemReservation> reservations = this.reservationRepository.findByLiteratureIdAndReserveeId(literatureId, reserveid);
			
			LiteratureItemReservation selected = reservations.get(0);
			int index = 1;
			while(selected.getStatus() != ReservationStatus.RESERVED && index < reservations.size())
				selected = reservations.get(index++);
				
				
			//Last part of the saga, notify that the book is reserved
			if(selected.getStatus() == ReservationStatus.RESERVED) {
				selected.setStatus(ReservationStatus.COMPLETE);
				this.reservationRepository.save(selected);
				this.buyItemSaga.notifyReservationComplete(literatureId, reserveid);
			}
			
			
			item.getReservees().remove(0);
		}
		item = this.literatureItemRepository.save(item);
		
		return item;
	}
	//If a certain book is not in the library, it can be requested from the bookstore to be available in the library
	public LiteratureItem requestBookInLibrary(String title, String author) throws CursusServiceException{
		if(isValidBook(title, author))
			throw new CursusServiceException("Book is already available in library");
		
		String bookstore_uri = String.format("http://apigateway:8080/api/courses/fakebookstore/price?title=%s&author=%s", 
				title, author);

		//Make call to the bookstore and request the price
		WebClient client = WebClient.create();
		try {
			double price = client.get().uri(bookstore_uri).exchange().timeout(Duration.ofMillis(5000))
					.flatMap(response -> response.bodyToMono(Double.class))
					.block();
			
			LiteratureItem item = new LiteratureItem(title, author, price, "BExxxbookstorexxx", 0, true, new byte[0]);
			return this.literatureItemRepository.save(item);
		} catch(TimeoutException ex) {
			throw new CursusServiceException("Bookstore could not be contacted");
		} catch(Exception ex) {
			throw new CursusServiceException(ex.getMessage());
		}
	}
	//Used for buying items from the library
	public void buyItem(String literatureId, String userId, PaymentMethod method) throws CursusServiceException{
		//Test if item exists
		if(!isValidLiteratureId(literatureId))
			throw new CursusServiceException("Item " + literatureId + " does not exist");
		//Test if buyer is a valid buyer
		if(!isValidBuyerId(userId))
			throw new CursusServiceException("Could not verify the buyer: " + userId);
		
		LiteratureItem item = this.literatureItemRepository.findById(literatureId).get();
		LiteratureItemReservation reservation = new LiteratureItemReservation(userId, literatureId);
		if(this.reservationRepository.save(reservation).getId() == null)
			throw new CursusServiceException("Failed to create reservation");
		
		synchronized(this.buyItemSaga) {
			this.buyItemSaga.startBuyItemSaga(reservation, item, method);
		}
		
	}
	
	public void registerListener(BuyItemCompleteListener listener) {
		this.buyItemSaga.registerListener(listener);
	}
	
	//Used for cleaning database when paymentservice is not active and a timeout occurs
	public void setFailedPayments(String literatureId, String reserveeId) {
		List<LiteratureItemReservation> reservations = this.reservationRepository.findByLiteratureIdAndReserveeId(literatureId, reserveeId);
		for(LiteratureItemReservation reservation : reservations) {
			if(reservation.getStatus() == ReservationStatus.PENDING) {
				reservation.setStatus(ReservationStatus.CANCELLED);
			}
		}
		this.reservationRepository.saveAll(reservations);
	}

	//When a student bought a literature-item, try to reserve the item and let the student know
	//if the item is ready retrieval
	private synchronized boolean reserveItem(String literatureId, String reserveeId) {
		LiteratureItem item = this.literatureItemRepository.findById(literatureId).get();
		boolean reserved = false;
		//Items are available and noone is waiting for a reservation
		if(item.getAmountAvailable() > 0 && item.getReservees().size() == 0) {
			item.setAmountAvailable(item.getAmountAvailable() - 1);
			reserved = true;
		} else {
			List<String> reservees = item.getReservees();
			reservees.add(reserveeId);
			item.setReservees(reservees);
			reserved = false;
		}
		this.literatureItemRepository.save(item);
		return reserved;
	}
	//Returns the debt the school has
	public HashMap<String,Double> getDebt(){
		return this.debt;
	}
	
	private Long requestStudentCount() {
		WebClient client = WebClient.create();
		try {
			Long amount = client.get().uri("http://apigateway:8080/api/students/count").exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(2000))
					.flatMap(response -> response.bodyToMono(Long.class))
					.block();
			this.logger.info("Response from studentservice: " + amount);
			return amount == null || amount < 1 ? 10l : amount;
		} catch(Exception e) {
			this.logger.info("Error while fetching studentcount: " + e.getMessage());
			return 10l;
		}
	}
	
	//---------------METHODS USED FOR VERIFYING-----------------------
	//Test if id is in the database
	private boolean isValidLiteratureId(String literatureId) {
		return this.literatureItemRepository.findById(literatureId).orElse(new LiteratureItem()).getId() != null;
	}
	//Test if book title and author are in databases
	private boolean isValidBook(String title, String author) {
		return this.literatureItemRepository.findByTitleAndAuthor(title, author).orElse(new LiteratureItem()).getId() != null;
	}
	//Test if the buyer is indeed a student
	private boolean isValidBuyerId(String buyerId) throws CursusServiceException{
		WebClient client = WebClient.create();
		try {
			String student = client.get().uri("http://apigateway:8080/api/students/" + buyerId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from studentservice: " + student);
			if(student == null)
				throw new CursusServiceException("Invalid buyer id");
			return student.contains("studentNumber");
		} catch(Exception e) {
			throw new CursusServiceException("Could not verify buyer id");
		}
	}
	
	private boolean isValidProfId(String profId) throws CursusServiceException{
		WebClient client = WebClient.create();
		try {
			String prof = client.get().uri("http://apigateway:8080/api/professors/" + profId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from profservice: " + prof);
			if(prof == null) {
				throw new CursusServiceException("Invalid profid");
			}
			return prof.contains("employeeNumber");
		} catch(Exception e) {
			throw new CursusServiceException("Could not verify prof id");
		}
	}
	
	//-------------METHODS USED BY THE SAGA/EVENTHANDLER-----------------------
	//When the payout from the school failed -> add amount to debt
	public synchronized void payoutFailed(String receiverId, double amount) {
		//Accumulate how much we owe to who
		if(!this.debt.containsKey(receiverId)) {
			this.debt.put(receiverId, amount);
		} else {
			this.debt.replace(receiverId, this.debt.get(receiverId) + amount);
		}
	}
	//If the student payed successfully
	public synchronized void buyPaymentSuccessful(String reservationId) {
		final LiteratureItemReservation reservation = this.reservationRepository.findById(reservationId).get();
		
		//Make sure "at-least-once" messages dont get reserved twice
		if(reservation.getStatus() == ReservationStatus.COMPLETE || reservation.getStatus() == ReservationStatus.RESERVED)
			return;
		
		reservation.setStatus(ReservationStatus.PAID);
		//Payment ok, we reserve the item
		this.logger.info("Buyer payed successfully, trying to reserve the item");
		if(this.reserveItem(reservation.getLiteratureId(), reservation.getReserveeId())) {
			reservation.setStatus(ReservationStatus.COMPLETE);
		} else {
			reservation.setStatus(ReservationStatus.RESERVED);
		}
		this.buyItemSaga.onBuyItemSuccessful(reservation);
		this.reservationRepository.save(reservation);
	}
	//If the payment from the student was not successful
	public synchronized void buyPaymentFailed(String reservationId) {
		final LiteratureItemReservation reservation = this.reservationRepository.findById(reservationId).get();
		reservation.setStatus(ReservationStatus.CANCELLED);
		this.buyItemSaga.onBuyItemCancelled(reservation);
		this.reservationRepository.save(reservation);
	}
	
	
	
}
