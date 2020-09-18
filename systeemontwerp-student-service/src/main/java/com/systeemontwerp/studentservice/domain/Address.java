package com.systeemontwerp.studentservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Address {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private String street;
	
	private String houseNumber;
	
	private String appartmentNumber;
	
	private String zipCode;
	
	private String city;
	
	private String state;
	
	private String country;
	
	@OneToOne(mappedBy = "address")
	private Student student;
	
	private Address() {}
	
	public Address(String street, String houseNumber, String appartmentNumber, String zipCode, String city,
			String state, String country) {
		this.street = street;
		this.houseNumber = houseNumber;
		this.appartmentNumber = appartmentNumber;
		this.zipCode = zipCode;
		this.city = city;
		this.state = state;
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getAppartmentNumber() {
		return appartmentNumber;
	}

	public void setAppartmentNumber(String appartmentNumber) {
		this.appartmentNumber = appartmentNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
