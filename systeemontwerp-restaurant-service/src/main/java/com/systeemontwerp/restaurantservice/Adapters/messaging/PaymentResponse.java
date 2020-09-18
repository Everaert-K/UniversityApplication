package com.systeemontwerp.restaurantservice.Adapters.messaging;

public class PaymentResponse {
	private String from;
	private String to;
	private PaymentStatus status;
	private double amount;
	private String itemIdentifier;
	private String id;
	
	public PaymentResponse(String id, String itemIdentifier, String from, String to, double amount, PaymentStatus status) {
		this.id = id;
		this.itemIdentifier = itemIdentifier;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemIdentifier() {
		return itemIdentifier;
	}

	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("payment[from=%s; to=%s; amount=%.2f; method=%s]", 
				from, to, amount, status.toString());
	}
}
