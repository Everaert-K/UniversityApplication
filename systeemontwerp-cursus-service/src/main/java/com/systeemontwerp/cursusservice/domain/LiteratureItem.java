package com.systeemontwerp.cursusservice.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class LiteratureItem {

	@Id
	private String id;
	private String title;
	private String author;
	private double price;
	private String bankNumber;
	private boolean isBook;
	
	//Only for cursusses
	private byte[] data;
	
	private int amountAvailable;
	//List of ids who are waiting for their book
	private List<String> reservees;
	
	//Nullitem
	public LiteratureItem() {}
	
	public LiteratureItem(String title, String author, double price, String bankNumber,int amountAvailable, boolean isBook, byte[] data) {
		this.title = title;
		this.author = author;
		this.price = price;
		this.amountAvailable = amountAvailable;
		this.isBook = isBook;
		this.data = data;
		this.bankNumber = bankNumber;
		
		reservees = new ArrayList<>();
	}
	
	@Override
	public String toString() {
		return String.format("literatureItem:[id=%s, title=%s, author=%s, price=%.2f, is_a_book=%s]", 
				this.id, this.title, this.author, this.price, String.valueOf(this.isBook));
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankNumber() {
		return bankNumber;
	}

	public void setBankNumber(String bankNumber) {
		this.bankNumber = bankNumber;
	}

	public boolean isBook() {
		return isBook;
	}

	public void setBook(boolean isBook) {
		this.isBook = isBook;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getAmountAvailable() {
		return amountAvailable;
	}

	public void setAmountAvailable(int amountAvailable) {
		this.amountAvailable = amountAvailable;
	}

	public List<String> getReservees() {
		return reservees;
	}

	public void setReservees(List<String> reservees) {
		this.reservees = reservees;
	}
	
	
	
	
}
