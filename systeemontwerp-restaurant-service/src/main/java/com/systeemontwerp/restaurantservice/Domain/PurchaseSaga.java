package com.systeemontwerp.restaurantservice.Domain;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systeemontwerp.restaurantservice.Adapters.messaging.DiscountApplication;
import com.systeemontwerp.restaurantservice.Adapters.messaging.RestaurantMessageGateway;

@Service
public class PurchaseSaga {
	
	private static Logger logger = LoggerFactory.getLogger(PurchaseSaga.class);
	
	private final RestaurantMessageGateway gateway;
	
	@Autowired
	public PurchaseSaga(RestaurantMessageGateway gateway) {
		this.gateway = gateway;
	}
	
	public void startAankoopSaga(Purchase p) {
		logger.info("aankoop saga started. Requesting discount and payment");
		p.setStatus(PurchaseStatus.ORDERED);
		// gateway.askDiscount // find a way to check if you earn a discount
		DiscountApplication a = new DiscountApplication(p.getId(),p.getFrom());
		gateway.emitProfDiscountApplication(a);
		gateway.emitStudDiscountApplication(a);
	}
	

	
}
