package com.systeemontwerp.cursusservice.adapteres.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CursusMessageGateway {

	@Gateway(requestChannel = MessagingChannels.LITERATUREITEM_RESERVED)
	void emitReservationNotice(ReservationNotice notice);
	
	@Gateway(requestChannel = MessagingChannels.CREATE_PAYMENT)
	void createPayment(PaymentRequest request);
	
	@Gateway(requestChannel = MessagingChannels.PAYMENT_OK)
	void handlePaymentOk(PaymentResponse response);
	
	@Gateway(requestChannel = MessagingChannels.PAYMENT_FAILED)
	void handlePaymentFailed(PaymentResponse response);
}
