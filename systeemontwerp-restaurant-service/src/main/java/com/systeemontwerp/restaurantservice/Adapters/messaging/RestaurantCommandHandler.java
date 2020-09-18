package com.systeemontwerp.restaurantservice.Adapters.messaging;

import org.springframework.stereotype.Service;

import com.systeemontwerp.restaurantservice.Adapters.rest.MenuItemRestController;
import com.systeemontwerp.restaurantservice.Adapters.rest.PurchaseRestController;
import com.systeemontwerp.restaurantservice.Domain.OrderFailedException;
import com.systeemontwerp.restaurantservice.Domain.MenuItem;
import com.systeemontwerp.restaurantservice.Domain.Purchase;
import com.systeemontwerp.restaurantservice.Domain.PurchaseStatus;
import com.systeemontwerp.restaurantservice.Domain.RestaurantService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;

@Service
public class RestaurantCommandHandler {
	
	private static Logger logger = LoggerFactory.getLogger(RestaurantEventHandler.class);
	private final RestaurantService restaurantservice;
	private final RestaurantMessageGateway messageGateway;
	private final RestaurantMessageGateway gateway;
	
	@Autowired
	private RestaurantCommandHandler(RestaurantService restaurantservice, RestaurantMessageGateway gateway, RestaurantMessageGateway gw) {
		logger.info("Create the command handler");
		this.restaurantservice = restaurantservice;
		this.messageGateway = gateway;
		this.gateway = gw;
	}
	
	
	
}






















