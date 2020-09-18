package com.systeemontwerp.restaurantservice.Adapters.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.restaurantservice.Domain.MenuItem;
import com.systeemontwerp.restaurantservice.Domain.Purchase;
import com.systeemontwerp.restaurantservice.Domain.PurchaseStatus;
import com.systeemontwerp.restaurantservice.Domain.RestaurantService;

@Service
public class RestaurantEventHandler {
	
	private static Logger logger = LoggerFactory.getLogger(RestaurantEventHandler.class);
	private final RestaurantService restaurantservice;
	
	@Autowired
	private RestaurantEventHandler(RestaurantService restaurantservice) {
		logger.info("Create the eventhandler");
		this.restaurantservice = restaurantservice;
	}
	
	@StreamListener(MessagingChannels.PAYMENT_OK)
	public void handlePaymentOk(PaymentResponse response) {
		this.logger.info("Payment for " + response.getItemIdentifier() + " succeeded");
		this.restaurantservice.buyPaymentSuccessful(response.getItemIdentifier());
	}
	
	@StreamListener(MessagingChannels.PAYMENT_FAILED) // nog doen
	public void handlePaymentFailed(PaymentResponse response) {
		//If payment from user failed, book can not be bought
		//If payment from scoop failed, add to debt
		if(!response.getItemIdentifier().equals("payout")) {
			this.logger.info("Payment from buyer not successful");			
			//this.cursusService.buyPaymentFailed(response.getItemIdentifier());
		} else {
			this.logger.info("Payment from school to prof/bookstore not successfull");
			//this.cursusService.payoutFailed(response.getTo(), response.getAmount());
		}
	}
	
	/*
	
	@StreamListener(MessagingChannels.PAYMENT_OK)
	public void processPaymentOK(PaymentResponse response) { 
		// final phase of purchase if you're here it's OK
		logger.info("Got payment OK for : " + response.getId());
		Purchase aankoop = this.restaurantservice.purchaseById(response.getId());
		aankoop.setStatus(PurchaseStatus.BOUGHT); // purchase succeeded and finished
		this.restaurantservice.postPurchase(aankoop);
		// update the database
		for(MenuItem item: aankoop.getPurchases().keySet()) {
			int aantalGekocht = aankoop.getPurchases().get(item);
			String id = item.getName();
			MenuItem i = this.restaurantservice.menuItemById(id);
			int aantalInVooraad = i.getQuantity();
			int over = aantalInVooraad - aantalGekocht;
			i.setQuantity(over);
			this.restaurantservice.postMenuItem(i);
		}
		// haal deferred result er uit !!!!!!
	}
	
	@StreamListener(MessagingChannels.PAYMENT_FAILED)
	public void processPaymentFailed(PaymentResponse response) {
		// save purchase with status PAYMENT_FAILED
		logger.info("Got payment FAIL for : " + response.getId());
		Purchase aankoop = this.restaurantservice.purchaseById(response.getId());
		aankoop.setStatus(PurchaseStatus.PAYMENT_FAILED);
	}
	*/
	
}
