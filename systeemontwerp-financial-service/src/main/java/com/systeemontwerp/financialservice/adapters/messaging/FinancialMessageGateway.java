package com.systeemontwerp.financialservice.adapters.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface FinancialMessageGateway {
	
	@Gateway(requestChannel = MessagingChannels.CREATE_PAYMENT)
	public void createPayment(PaymentRequest request);
	
	@Gateway(requestChannel = MessagingChannels.PAYMENT_OK)
	public void emitPaymentOk(PaymentResponse response);
	
	@Gateway(requestChannel = MessagingChannels.PAYMENT_FAILED)
	public void emitPaymentFailed(PaymentResponse response);
	
}
