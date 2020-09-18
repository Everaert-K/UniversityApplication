package com.systeemontwerp.roosterservice.domain;

public class RoosterFailedException extends Exception{
	
	public RoosterFailedException(String reason) {
		super("Rooster not successful: " + reason);
	}
}
