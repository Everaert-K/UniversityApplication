package com.systeemontwerp.financialservice.domain;

public class FinancialServiceException extends Exception {
	public FinancialServiceException(String reason) {
		super("Payment not successful: " + reason);
	}
}
