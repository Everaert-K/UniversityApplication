package com.systeemontwerp.cursusservice.adapteres.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessagingChannels {

	static final String LITERATUREITEM_RESERVED = "literatureitem_reserved";
	
	static final String CREATE_PAYMENT = "create_payment";
	static final String PAYMENT_OK = "payment_ok";
	static final String PAYMENT_FAILED = "payment_failed";
	
	@Output(LITERATUREITEM_RESERVED)
	MessageChannel literatureItemReserved();
	
	
	//MessagingChannels from financial service
	@Output(CREATE_PAYMENT)
	MessageChannel createPayment();
	
	@Input(PAYMENT_OK)
	SubscribableChannel paymentOk();
	
	@Input(PAYMENT_FAILED)
	SubscribableChannel paymentFailed();
		
}
