package com.systeemontwerp.cursusservice.adapteres.messaging;

public class PaymentRequest {
	
	//To identify what the payment is for
	private String itemIdentifier;
	private String from;
	private String to;
	private PaymentMethod method;
	private double amount;
	
	public PaymentRequest(String itemIdentifier, String from, String to, double amount, PaymentMethod method) {
		this.itemIdentifier = itemIdentifier;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.method = method;
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

	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		return String.format("payment[from=%s; to=%s; amount=%.2f; method=%s]", 
				from, to, amount, method.toString());
	}
	
	
}
