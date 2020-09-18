package com.systeemontwerp.reserveringservice.adapters.messaging;

import java.time.LocalDate;
import java.util.List;

import com.systeemontwerp.reserveringservice.domain.Reservering;
import com.systeemontwerp.reserveringservice.domain.ReserveringType;
import com.systeemontwerp.reserveringservice.domain.Time;

public class ReserveRoomResponse {

	private String id;
	private String lokaalId;
	private String profId;
	private LocalDate date;
	private Time time;
	private ReserveringType type;
	private String status;
	private List<String> hourIds;

	public ReserveRoomResponse(String id, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type, String status, List<String> hourIds) {
		this.id = id;
		this.profId = profId;
		this.lokaalId = lokaalId;
		this.date = date;
		this.time = time;
		this.status = status;
		this.type = type;
		this.hourIds = hourIds;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getLokaalId() {
		return lokaalId;
	}
	public void setLokaalId(String lokaalId) {
		this.lokaalId = lokaalId;
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
