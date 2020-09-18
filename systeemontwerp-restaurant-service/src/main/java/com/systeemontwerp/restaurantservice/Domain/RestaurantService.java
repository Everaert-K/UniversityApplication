package com.systeemontwerp.restaurantservice.Domain;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.restaurantservice.Adapters.messaging.PaymentRequest;
import com.systeemontwerp.restaurantservice.Adapters.messaging.PaymentResponse;
import com.systeemontwerp.restaurantservice.Adapters.messaging.PaymentStatus;
import com.systeemontwerp.restaurantservice.Adapters.messaging.RestaurantMessageGateway;
import com.systeemontwerp.restaurantservice.Adapters.rest.MenuItemRestController;
import com.systeemontwerp.restaurantservice.Adapters.rest.PurchaseRestController;
import com.systeemontwerp.restaurantservice.Persistence.MenuItemRepository;
import com.systeemontwerp.restaurantservice.Persistence.PurchaseRepository;

@Service
public class RestaurantService {
	private static Logger logger = LoggerFactory.getLogger(RestaurantService.class);
	
	private final MenuItemRepository menurepo;
	private final PurchaseRepository purchaserepo;
	private final RestaurantMessageGateway gateway;
	
	private List<BuyItemCompleteListener> listeners;
	
	private HashMap<String,DeferredResult<PaymentResponse>> map;
	
	@Autowired
	public RestaurantService(MenuItemRepository menurepo, PurchaseRepository purchaserepo, RestaurantMessageGateway gateway) {
		this.menurepo = menurepo;
		this.purchaserepo = purchaserepo;
		this.gateway = gateway;
		this.listeners = new ArrayList<>();
	}
	
	
	public Purchase purchaseById(String id) {
		return this.purchaserepo.findById(id).orElse(null);
	}
	
	
	public void postPurchase(Purchase p) {
		//this.purchasecontroller.postPurchase(p);
		this.purchaserepo.save(p);
	}
	
	
	public MenuItem menuItemById(String id) {
		//return this.menucontroller.menuItemById(id);
		return this.menurepo.findById(id).orElse(null);
	}
	
	public void postMenuItem(MenuItem item) {
		//this.menucontroller.postMenuItem(item);
		this.menurepo.save(item);
	}
	
	
	public Purchase makePurchase(HashMap<String,Integer> bestelling, String id, String from, String method) throws OrderFailedException {
		// if id == -1 than don't bother checking it
		boolean korting = false;
		if(!id.equals("-1")) {
			// check of valid student or professor
			try {
				korting = this.isValidStudentId(id);
			} catch (OrderFailedException e) {
				// do nothing, already false
			}
			if(!korting) {
				try {
					korting = this.isValidProfId(id);
					from = id;
				} catch (OrderFailedException e) {
					// do nothing, already false
				}
			}
			if(!korting) {
				throw new OrderFailedException("Invalid ID!");
			}
		}
		if(!korting) {
			from = "extern";
		}
		else {
			from = id;
		}
		HashMap<MenuItem,Integer> purchases = new HashMap<MenuItem,Integer>();
		for(String item : bestelling.keySet()) {
			MenuItem menuItem = this.menurepo.findById(id).orElse(null);
			purchases.put(menuItem, bestelling.get(item));
		}
		PaymentMethod paymentMethod;
		if(method.equals("BANCONTACT")) {
			paymentMethod = PaymentMethod.BANCONTACT;
		}
		else if(method.equals("PAYPAL")) {
			paymentMethod = PaymentMethod.PAYPAL;
		}
		else {
			paymentMethod = PaymentMethod.EPURSE;
		}
		Purchase purchase = new Purchase(from,"schoolID",purchases,korting,paymentMethod);
		this.purchaserepo.save(purchase);
		// create payment
		// maak deferredresult op 10 sec
		// maak_bestelling
		PaymentRequest request = new PaymentRequest(purchase.getId(),from,"schoolID",purchase.calculatePrice(),paymentMethod);
		this.gateway.askPayment(request);
		return purchase; 
	}
	
	
	/////// checks to see if the profid or student id was valid ////////
	
	private boolean isValidStudentId(String buyerId) throws OrderFailedException{
		WebClient client = WebClient.create();
		try {
			String student = client.get().uri("http://apigateway:8080/api/students/" + buyerId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from studentservice: " + student);
			if(student == null)
				throw new OrderFailedException("Invalid student id");
			return student.contains("studentNumber");
		} catch(Exception e) {
			throw new OrderFailedException("Could not verify student id");
		}
	}
	
	private boolean isValidProfId(String profId) throws OrderFailedException{
		WebClient client = WebClient.create();
		try {
			String prof = client.get().uri("http://apigateway:8080/api/professors/" + profId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from profservice: " + prof);
			if(prof == null) {
				throw new OrderFailedException("Invalid profid");
			}
			return prof.contains("employeeNumber");
		} catch(Exception e) {
			throw new OrderFailedException("Could not verify prof id");
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	//If the student payed successfully
	public void buyPaymentSuccessful(String purchaseId) {
		Purchase purchase = this.purchaserepo.findById(purchaseId).get();
		
		//Make sure "at-least-once" messages dont get reserved twice
		if(purchase.getStatus() == PurchaseStatus.BOUGHT)
			return;
		
		purchase.setStatus(PurchaseStatus.BOUGHT);
		//Payment ok, we reserve the item
		this.logger.info("Buyer payed successfully");
		
		for(MenuItem item: purchase.getPurchases().keySet()) {
			int aantalGekocht = purchase.getPurchases().get(item);
			String id = item.getName();
			MenuItem i = this.menurepo.findById(id).get();
			int aantalInVooraad = i.getQuantity();
			int over = aantalInVooraad - aantalGekocht;
			i.setQuantity(over);
			this.menurepo.save(i);
		}
		
		this.listeners.forEach(listener -> listener.onBuyItemComplete(purchase));
		
		this.purchaserepo.save(purchase);
	}
	
	// buyPaymentFailed
	// listeners ook laten weten in failed
	public void purchaseFailed(String purchaseId) {
		logger.info("Payment failed");
		Purchase purchase = this.purchaserepo.findById(purchaseId).get();
		purchase.setStatus(PurchaseStatus.PAYMENT_FAILED);
		this.listeners.forEach(listener -> listener.onBuyItemComplete(purchase));
		this.purchaserepo.save(purchase);
	}


	public void registerListener(BuyItemCompleteListener listener) {
			this.listeners.add(listener);
	}

	
}








