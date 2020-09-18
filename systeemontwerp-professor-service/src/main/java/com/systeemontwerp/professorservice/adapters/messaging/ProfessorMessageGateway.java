package com.systeemontwerp.professorservice.adapters.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface ProfessorMessageGateway {

	@Gateway(requestChannel = Channels.PROFESSOR_DISCOUNT_REQUEST)
	void handleDiscountRequest(DiscountApplication application);
	
	@Gateway(requestChannel = Channels.PROFESSOR_DISCOUNT_RESPONSE)
	void emitDiscountResponse(DiscountResponse response);
	
}
