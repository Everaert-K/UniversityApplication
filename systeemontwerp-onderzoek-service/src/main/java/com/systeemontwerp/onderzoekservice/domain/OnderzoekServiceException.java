package com.systeemontwerp.onderzoekservice.domain;

public class OnderzoekServiceException extends Exception {
	public OnderzoekServiceException(String reason) {
		super("Research request failed:"+reason);
	}
}
