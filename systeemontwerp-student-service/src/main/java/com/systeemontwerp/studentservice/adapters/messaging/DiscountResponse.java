package com.systeemontwerp.studentservice.adapters.messaging;

public class DiscountResponse {
	
	private String id;
	private boolean discount;

	public DiscountResponse(String id, boolean korting) {
		this.id = id; // id of purchase
		this.discount = korting; // if the student receives a discount
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "DiscountResponse [id=" + id + ", korting=" + discount + "]";
	}

	public void setDiscount(boolean korting) {
		this.discount = korting;
	}
	
	public boolean isDiscount() {
		return this.discount;
	}
	
}
