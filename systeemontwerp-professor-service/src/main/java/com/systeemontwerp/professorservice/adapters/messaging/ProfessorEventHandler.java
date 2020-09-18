package com.systeemontwerp.professorservice.adapters.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.professorservice.domain.ProfessorService;

@Service
public class ProfessorEventHandler {
	
	private final ProfessorService professorService;
	private final ProfessorMessageGateway messageGateway;
	
	@Autowired
	public ProfessorEventHandler(ProfessorService professorService, ProfessorMessageGateway messageGateway) {
		this.professorService = professorService;
		this.messageGateway = messageGateway;
	}
	
	@StreamListener(Channels.PROFESSOR_DISCOUNT_REQUEST)
	public void handleDiscountRequest(DiscountApplication application) {
		boolean professorExists = this.professorService.doesProfessorExist(application.getNummer());
		DiscountResponse response = new DiscountResponse(application.getId(), professorExists);
		messageGateway.emitDiscountResponse(response);
	}
	
}
