package com.systeemontwerp.cursusservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LiteratureItemReservation {

	@Id
	private String id;
	private String reserveeId;
	private String literatureId;
	private ReservationStatus status;
	
	//Nullitem
	public LiteratureItemReservation() {}
	
	public LiteratureItemReservation(String reserveeId, String literatureId) {
		this.reserveeId = reserveeId;
		this.literatureId = literatureId;
		this.status = ReservationStatus.PENDING;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReserveeId() {
		return reserveeId;
	}

	public void setReserveeId(String reserveeId) {
		this.reserveeId = reserveeId;
	}

	public String getLiteratureId() {
		return literatureId;
	}

	public void setLiteratureId(String literatureId) {
		this.literatureId = literatureId;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	
	
}
