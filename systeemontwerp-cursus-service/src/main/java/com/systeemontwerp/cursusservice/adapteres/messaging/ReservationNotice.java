package com.systeemontwerp.cursusservice.adapteres.messaging;

public class ReservationNotice {
	
	private String literatureId;
	private String buyerId;
	
	public ReservationNotice(String literatureId, String buyerId) {
		this.literatureId = literatureId;
		this.buyerId = buyerId;
	}

	public String getLiteratureId() {
		return literatureId;
	}

	public void setLiteratureId(String literatureId) {
		this.literatureId = literatureId;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}
	
	
}
