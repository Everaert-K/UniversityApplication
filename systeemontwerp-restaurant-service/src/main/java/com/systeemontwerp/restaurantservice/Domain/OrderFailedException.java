package com.systeemontwerp.restaurantservice.Domain;

public class OrderFailedException extends Exception{ 
	public OrderFailedException(String reason) {
		super("Order not successful: " + reason);
	}
}
