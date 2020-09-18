package com.systeemontwerp.studentservice.adapters.messaging;

public class DiscountApplication {
	
	private String id;
	private String nummer;

	public DiscountApplication(String id, String nummer) {
		this.id = id; // id of purchase
		this.nummer = nummer; // studentnumber
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNummer() {
		return nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}
	
}
