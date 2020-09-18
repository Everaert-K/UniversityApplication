package com.systeemontwerp.studentservice.adapters.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.systeemontwerp.studentservice.domain.Student;

@MessagingGateway
public interface StudentMessageGateway {
	
	@Gateway(requestChannel = Channels.CREATE_PAYMENT)
	void createPayment(PaymentRequest request);
	
	@Gateway(requestChannel = Channels.EMIT_REGISTRATION_SUCCESSFUL)
	void emitRegistrationSuccessful(Student student);
	
	@Gateway(requestChannel = Channels.PAYMENT_OK)
	void handlePaymentOk(PaymentResponse response);
	
	@Gateway(requestChannel = Channels.PAYMENT_FAILED)
	void handlePaymentFailed(PaymentResponse response);
	
	@Gateway(requestChannel = Channels.STUDENT_DISCOUNT_REQUEST)
	void handleDiscountRequest(DiscountApplication application);
	
	@Gateway(requestChannel = Channels.STUDENT_DISCOUNT_RESPONSE)
	void emitDiscountResponse(DiscountResponse response);
	
}
