package com.systeemontwerp.financialservice.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class PaymentRecord {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String itemIdentifier;
	private double amount;
	private String fromId;
	private String toId;
	private PaymentStatus status;
	private PaymentMethod method;
	private LocalDate creationDate;
	
	//NUllrecord
	public PaymentRecord() {}
	
	public PaymentRecord(String itemIdentifier, String fromId, String toId, double amount, PaymentMethod method) {
		this.itemIdentifier = itemIdentifier;
		this.amount = amount;
		this.fromId = fromId;
		this.toId = toId;
		this.method = method;
		this.status = PaymentStatus.PENDING;
		this.creationDate = LocalDate.now();
	}
	
	@Override
	public String toString() {
		return String.format("Payment[id=%s, from=%s, to=%s, amount=%.2f, method=%s, status=%s]",
				id, fromId, toId, amount, method.toString(), status.toString());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemIdentifier() {
		return itemIdentifier;
	}

	public void setItemIdentifier(String itemIdentifier) {
		this.itemIdentifier = itemIdentifier;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
	
	

}
