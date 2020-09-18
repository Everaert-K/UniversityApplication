package com.systeemontwerp.roosterservice.adapters.messaging;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.systeemontwerp.roosterservice.domain.Time;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduleClassRequest {

	private Long id;
	private String vakId;
	private LocalDate day;
	private Time time;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVakId() {
		return vakId;
	}

	public void setVakId(String vakId) {
		this.vakId = vakId;
	}
	
	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}
