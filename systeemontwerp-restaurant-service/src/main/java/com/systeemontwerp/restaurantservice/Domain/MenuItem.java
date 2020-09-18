package com.systeemontwerp.restaurantservice.Domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class MenuItem implements Serializable{
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String name;
	private int quantity;
	private double price;
	private boolean fattaks;
	private Type type;
	
	private MenuItem() {}
	
	public MenuItem(String name, int quantity, double price, boolean fattaks, Type type){
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.fattaks = fattaks;
		this.type = type;
	}
	
	public String toString() {
		String zin = "naam: "+this.name+" hoeveelheid: "+this.quantity+" prijs: "+this.price+" type: "+this.type;
		if(this.fattaks) {
			zin+=" vettaks\n";
		}
		return zin;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// getters and setters
	public String getName() {
		return name;
	}
	public void setName(String naam) {
		this.name = naam;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int hoeveelheid) {
		this.quantity = hoeveelheid;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double prijs) {
		this.price = prijs;
	}
	public boolean isFattaks() {
		return fattaks;
	}
	public void setFattaks(boolean vettaks) {
		this.fattaks = vettaks;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public int hashCode() {
		String res = name + quantity + price+ fattaks + type;
		return res.hashCode();
	}
	
}
