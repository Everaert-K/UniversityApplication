package com.systeemontwerp.cursusservice.domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.cursusservice.adapteres.messaging.CursusMessageGateway;
import com.systeemontwerp.cursusservice.adapteres.messaging.PaymentMethod;
import com.systeemontwerp.cursusservice.adapteres.messaging.PaymentRequest;
import com.systeemontwerp.cursusservice.adapteres.messaging.ReservationNotice;

@Service
public class BuyItemSaga {
	
	private static Logger logger = Logger.getLogger(BuyItemSaga.class);
	
	private final CursusMessageGateway messageGateway;
	private List<BuyItemCompleteListener> listeners;
	
	@Autowired
	public BuyItemSaga(CursusMessageGateway messageGateway) {
		this.messageGateway = messageGateway;
		this.listeners = new ArrayList<>();
	}
	//Saga for buying an item from the cursuslibrary
	public void startBuyItemSaga(LiteratureItemReservation reservation, LiteratureItem item, PaymentMethod method) {
		//Student pays school
		PaymentRequest studentPayment = new PaymentRequest(reservation.getId(), reservation.getReserveeId(), "schoolID", item.getPrice(), method);
		//School pays prof or bookstore
		PaymentRequest schoolPayment =  new PaymentRequest("payout", "schoolID", item.getBankNumber(), item.getPrice(), PaymentMethod.BANCONTACT);
		//If its a prof, only 50% is paid out
		if(!item.isBook())
			schoolPayment.setAmount(Math.round(schoolPayment.getAmount() * 50.0) / 100.0);
		
		reservation.setStatus(ReservationStatus.PENDING);
		
		this.messageGateway.createPayment(studentPayment);
		
		//Books are already paid for when we restocked them
		if(!item.isBook())
			this.messageGateway.createPayment(schoolPayment);
	}
	
	//For the school so they can buy the books from the bookstore
	public void startBuyBookSaga(LiteratureItem item, int amount) {
		PaymentRequest schoolPayment = new PaymentRequest("payout", "schoolID", item.getBankNumber(), item.getPrice() * amount, PaymentMethod.BANCONTACT);
		this.messageGateway.createPayment(schoolPayment);
	}
	
	public void registerListener(BuyItemCompleteListener listener) {
		this.listeners.add(listener);
	}
	
	public void notifyReservationComplete(String literatureId, String reserveeId) {
		this.messageGateway.emitReservationNotice(new ReservationNotice(literatureId, reserveeId));
	}
	
	public void onBuyItemSuccessful(LiteratureItemReservation reservation) {
		//If the book was also available for the student, we let him/her know
		if(reservation.getStatus() == ReservationStatus.COMPLETE)
			this.notifyReservationComplete(reservation.getLiteratureId(), reservation.getReserveeId());
		this.listeners.forEach(listener -> listener.onBuyItemComplete(reservation));
	}
	
	public void onBuyItemCancelled(LiteratureItemReservation reservation) {
		this.listeners.forEach(listener -> listener.onBuyItemComplete(reservation));
	}
	
	
}
