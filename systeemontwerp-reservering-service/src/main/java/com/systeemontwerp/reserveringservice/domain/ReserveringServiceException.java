package com.systeemontwerp.reserveringservice.domain;

public class ReserveringServiceException extends Exception {
	public ReserveringServiceException(String reason) {
		super("Reservering not successful: " + reason);
	}
}
