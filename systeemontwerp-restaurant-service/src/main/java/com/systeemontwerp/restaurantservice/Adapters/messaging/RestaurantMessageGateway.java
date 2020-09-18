package com.systeemontwerp.restaurantservice.Adapters.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.systeemontwerp.restaurantservice.Domain.Purchase;


@MessagingGateway
public interface RestaurantMessageGateway {
	
	// output channel
	@Gateway(requestChannel = MessagingChannels.CREATE_PAYMENT)
	public void askPayment(PaymentRequest request); 
	
	@Gateway(requestChannel = MessagingChannels.PAYMENT_FAILED)
	public void receivePaymentFailed(PaymentResponse response);
	
	@Gateway(requestChannel = MessagingChannels.PAYMENT_OK)
	public void receivePaymentOk(PaymentResponse response); 
	 
	@Gateway(requestChannel = MessagingChannels.PROFESSOR_DISCOUNT_REQUEST)
	public void emitProfDiscountApplication(DiscountApplication appl);
	 
	@Gateway(requestChannel = MessagingChannels.STUDENT_DISCOUNT_REQUEST)
	public void emitStudDiscountApplication(DiscountApplication appl);
	 
	@Gateway(requestChannel = MessagingChannels.PROFESSOR_DISCOUNT_RESPONSE)
	public void receiveProfDiscountResponse(boolean antwoord);
	 
	@Gateway(requestChannel = MessagingChannels.STUDENT_DISCOUNT_RESPONSE)
	public void receiveStudDiscountResponse(boolean antwoord);
	
}
