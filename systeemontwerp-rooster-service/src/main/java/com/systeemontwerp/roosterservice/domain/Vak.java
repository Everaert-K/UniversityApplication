package com.systeemontwerp.roosterservice.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // MongoDB
public class Vak {
	@Id
	private String id;
	@Indexed(unique = true)
	private String name;
	private String profId;
	private String lokaalId;
	private VakStatus status;
	private int hours;
	private int hoursRostered;
	
	public Vak() {	}
	
	public Vak(String name, String profId, String lokaalId, int hours) {	
		this.name = name;
		this.profId = profId;
		this.lokaalId = lokaalId;
		this.hours = hours;
		this.hoursRostered = 0;
		this.status = VakStatus.UNCHEDULED;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfId() {
		return profId;
	}

	public void setProfId(String profId) {
		this.profId = profId;
	}

	public String getLokaalId() {
		return lokaalId;
	}

	public void setLokaalId(String lokaalId) {
		this.lokaalId = lokaalId;
	}

	public VakStatus getStatus() {
		return status;
	}

	public void setStatus(VakStatus status) {
		this.status = status;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getHoursRostered() {
		return hoursRostered;
	}

	public void setHoursRostered(int hoursRostered) {
		this.hoursRostered = hoursRostered;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Vak [id=" + id + ", name=" + name + ", profId=" + profId + ", lokaalId=" + lokaalId + ", status="
				+ status + ", hours=" + hours + ", hoursRostered=" + hoursRostered + "]";
	}
	
}
