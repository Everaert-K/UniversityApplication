package com.systeemontwerp.restaurantservice.Adapters.messaging;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;


public interface MessagingChannels {
	
	static final String CREATE_PAYMENT = "create_payment";
	static final String PAYMENT_OK = "payment_ok";
	static final String PAYMENT_FAILED = "payment_failed";
	
	static final String PROFESSOR_DISCOUNT_REQUEST = "professor_discount_request";
	static final String STUDENT_DISCOUNT_REQUEST = "student_discount_request";
	static final String PROFESSOR_DISCOUNT_RESPONSE = "professor_discount_response";
	static final String STUDENT_DISCOUNT_RESPONSE = "student_discount_response";
	
	@Output(CREATE_PAYMENT) 
	MessageChannel sendPayment();
	
	@Input(PAYMENT_OK)
	SubscribableChannel processPaymentOk();
	
	@Input(PAYMENT_FAILED)
	SubscribableChannel processPaymentFailed();
	
	@Output(PROFESSOR_DISCOUNT_REQUEST)
	MessageChannel emitProfessorDiscountApplication();
	
	@Output(STUDENT_DISCOUNT_REQUEST)
	MessageChannel emitStudentDiscountApplication();
	
	@Input(PROFESSOR_DISCOUNT_RESPONSE)
	SubscribableChannel receiveProfessorDiscountResponse();
	
	@Input(STUDENT_DISCOUNT_RESPONSE)
	SubscribableChannel receiveStudentDiscountResponse();
	
}
