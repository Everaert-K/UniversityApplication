package com.systeemontwerp.onderzoekservice.domain;

import java.time.LocalDate;

public class Hours {
	private String Id;
	
	private Time start;
	
	private Time end;
	
	private LocalDate day;
	
	private Hours() {}
	
	public Hours(Time start, Time end, LocalDate day) {
		this.start = start;
		this.end = end;
		this.day = day;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		this.Id = id;
	}

	public Time getStart() {
		return start;
	}

	public void setStart(Time start) {
		this.start = start;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

}
