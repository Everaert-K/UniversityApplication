package com.systeemontwerp.restaurantservice.Domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Purchase {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String from_id;
	private String to_id;
	@ElementCollection
	private Map<MenuItem,Integer> purchases; // map menuItem on the amount you buy of the item
	private boolean discount;
	private PurchaseStatus status;
	private PaymentMethod method;
	final static private int kortingswaarde = 20;
	
	private Purchase() {}
	
	public Purchase(String from_id, String to_id,HashMap<MenuItem,Integer> purchases,boolean discount,PaymentMethod method) {
		this.from_id = from_id;
		this.to_id = to_id;
		this.purchases = purchases;
		this.discount = discount;
		this.status = PurchaseStatus.ORDERED; 
		this.method = method;
	}

	public Map<MenuItem,Integer> getPurchases() {
		return purchases;
	}
	public void setPurchases(HashMap<MenuItem,Integer> aankopen) {
		this.purchases = aankopen;
	}
	
	public double calculatePrice() { 
		
		double price = 0;
		
		for(MenuItem m : this.purchases.keySet()) {
			int aantal = this.purchases.get(m);
			price += (aantal*m.getPrice());
		}
		
		if(discount) {
			price = price - (price*this.kortingswaarde/100);
		}
		
		return price;
	}

	public String getId() {
		return id;
	}

	public String getFrom() {
		return from_id;
	}

	public void setFrom(String from) {
		this.from_id = from;
	}

	public String getTo() {
		return to_id;
	}

	public void setTo(String to) {
		this.to_id = to;
	}

	public boolean isDiscount() {
		return discount;
	}

	public void setDiscount(boolean korting) {
		this.discount = korting;
	}

	public PurchaseStatus getStatus() {
		return status;
	}

	public void setStatus(PurchaseStatus status) {
		this.status = status;
	}

	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}
	
}
