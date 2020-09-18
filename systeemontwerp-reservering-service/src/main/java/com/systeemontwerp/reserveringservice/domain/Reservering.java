package com.systeemontwerp.reserveringservice.domain;


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document // MongoDB
@CompoundIndex(def = "{'lokaalId': 1, 'date': 1, 'time': 1}", unique = true)
public class Reservering {
	@Id
	private String id;
	private ReserveringType type;
	private String lokaalId;
	private String profId;
	private Time time;
	private LocalDate date;
	
	public Reservering() { }

	public Reservering(String profId, String lokaalId, ReserveringType type, LocalDate date, Time time) {
		this.type = type;
		this.lokaalId = lokaalId;
		this.profId = profId;
		this.date = date;
		this.time = time;
	}

	public ReserveringType getType() {
		return type;
	}

	public void setType(ReserveringType type) {
		this.type = type;
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

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
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

	@Override
	public String toString() {
		return "Reservering [id=" + id + ", type=" + type + ", lokaalId=" + lokaalId + ", profId=" + profId + ", time="
				+ time + ", date=" + date + "]";
	}
	
	
}
