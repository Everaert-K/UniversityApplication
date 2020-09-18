package com.systeemontwerp.financialservice.adapters.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessagingChannels {
	
	static final String CREATE_PAYMENT = "create_payment";
	static final String PAYMENT_OK = "payment_ok";
	static final String PAYMENT_FAILED = "payment_failed";
	
	@Output(PAYMENT_OK)
	MessageChannel paymentOk();
	
	@Output(PAYMENT_FAILED)
	MessageChannel paymentFailed();
	
	@Input(CREATE_PAYMENT)
	SubscribableChannel createPayment();

}
