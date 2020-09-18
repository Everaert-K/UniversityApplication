package com.systeemontwerp.roosterservice.adapters.messaging;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.systeemontwerp.roosterservice.domain.ReserveringType;
import com.systeemontwerp.roosterservice.domain.Time;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckHoursLokaalRequest {

	private String profId;
	private LocalDate date;
	private String id;
	private String lokaalId;
	private Time time;
	private ReserveringType type;
	private List<String> hourIds;

	public CheckHoursLokaalRequest(String id, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		this.id = id;
		this.profId = profId;
		this.lokaalId = lokaalId;
		this.date = date;
		this.time = time;
		this.type = type;
		this.hourIds = hourIds;
	}

	public String getProfId() {
		return profId;
	}

	public void setProfId(String profId) {
		this.profId = profId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLokaalId() {
		return lokaalId;
	}

	public void setLokaalId(String lokaalId) {
		this.lokaalId = lokaalId;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public ReserveringType getType() {
		return type;
	}

	public void setType(ReserveringType type) {
		this.type = type;
	}

	public List<String> getHourIds() {
		return hourIds;
	}

	public void setHourIds(List<String> hourIds) {
		this.hourIds = hourIds;
	}

	
}

