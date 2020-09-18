package com.systeemontwerp.studentservice.adapters.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {
	
	static final String CREATE_PAYMENT = "create_payment";
	static final String PAYMENT_OK = "payment_ok";
	static final String PAYMENT_FAILED = "payment_failed";
	static final String EMIT_REGISTRATION_SUCCESSFUL = "emit_registration_successful";
	static final String STUDENT_DISCOUNT_REQUEST = "student_discount_request";
	static final String STUDENT_DISCOUNT_RESPONSE = "student_discount_response";
	
	@Output(CREATE_PAYMENT)
	MessageChannel createPayment();
	
	@Output(EMIT_REGISTRATION_SUCCESSFUL)
	MessageChannel emitRegistrationSuccessful();
	
	@Input(PAYMENT_OK)
	SubscribableChannel paymentOk();
	
	@Input(PAYMENT_FAILED)
	SubscribableChannel paymentFailed();
	
	@Input(STUDENT_DISCOUNT_REQUEST)
	SubscribableChannel studentDiscountRequest();
	
	@Output(STUDENT_DISCOUNT_RESPONSE)
	MessageChannel studentDiscountResponse();
	
}
