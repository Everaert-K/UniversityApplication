package com.systeemontwerp.professorservice.adapters.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {
	
	static final String PROFESSOR_DISCOUNT_REQUEST = "professor_discount_request";
	static final String PROFESSOR_DISCOUNT_RESPONSE = "professor_discount_response";
	
	@Input(PROFESSOR_DISCOUNT_REQUEST)
	SubscribableChannel professorDiscountRequest();
	
	@Output(PROFESSOR_DISCOUNT_RESPONSE)
	MessageChannel professorDiscountResponse();
	
}
