package com.systeemontwerp.restaurantservice.Domain;

public enum PurchaseStatus {
	ORDERED,
	
	DISCOUNT_CHECK_PENDING,
	DISCOUNT_CHECKED,
	DISCOUNT_CHECK_FAILED,
	
	PAYMENT_PENDING,
	BOUGHT, 
	PAYMENT_FAILED,
	
	FAILED
}
