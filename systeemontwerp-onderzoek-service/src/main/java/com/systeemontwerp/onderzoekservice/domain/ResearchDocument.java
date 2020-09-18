package com.systeemontwerp.onderzoekservice.domain;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ResearchDocument {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private Status status;
	private ReserveStatus reserveStatus;
	private String description;
	private LocalDate terminationDate;
	private ArrayList<String> profIds;
	private double estimatedPrice;
	private String lokaalId;
	private int neededHours;
	private int scheduledHours = 0;
	
	private ResearchDocument() {}
	
	public ResearchDocument(String description, LocalDate terminationDate, ArrayList<String> profIds, double estimatedPrice, String lokaalId, int neededHours) {
		this.status = Status.REQUESTED;
		this.reserveStatus = ReserveStatus.UNRESERVED;
		this.description = description;
		this.terminationDate = terminationDate;
		this.profIds = profIds;
		this.estimatedPrice = estimatedPrice;
		this.lokaalId = lokaalId;
		this.neededHours = neededHours;
		this.scheduledHours = 0;
	}
	
	public ResearchDocument(ResearchDocument d) {
		this.status = Status.REQUESTED;
		this.description = d.getDescription();
		this.terminationDate = d.getTerminationDate();
		this.profIds = d.getProfIds();
		this.estimatedPrice = d.getEstimatedPrice();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(LocalDate terminationDate) {
		this.terminationDate = terminationDate;
	}

	public ArrayList<String> getProfIds() {
		return profIds;
	}

	public void setProfIds(ArrayList<String> profIds) {
		this.profIds = profIds;
	}

	public double getEstimatedPrice() {
		return estimatedPrice;
	}

	public void setEstimatedPrice(double estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}

	
	public ReserveStatus getReserveStatus() {
		return reserveStatus;
	}

	public void setReserveStatus(ReserveStatus reserveStatus) {
		this.reserveStatus = reserveStatus;
	}

	public String getLokaalId() {
		return lokaalId;
	}

	public void setLokaalId(String lokaalId) {
		this.lokaalId = lokaalId;
	}

	public int getNeededHours() {
		return neededHours;
	}

	public void setNeededHours(int neededHours) {
		this.neededHours = neededHours;
	}

	public int getScheduledHours() {
		return scheduledHours;
	}

	public void setScheduledHours(int scheduledHours) {
		this.scheduledHours = scheduledHours;
	}

	@Override
	public String toString() {
		return "ResearchDocument [id=" + id + ", status=" + status + ", reserveStatus=" + reserveStatus
				+ ", description=" + description + ", terminationDate=" + terminationDate + ", profIds=" + profIds
				+ ", estimatedPrice=" + estimatedPrice + ", lokaalId=" + lokaalId + ", neededHours=" + neededHours
				+ ", scheduledHours=" + scheduledHours + "]";
	}
	
	
	
}
