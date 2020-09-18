package com.systeemontwerp.financialservice.adapters.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.financialservice.domain.FinancialService;
import com.systeemontwerp.financialservice.domain.FinancialServiceException;
import com.systeemontwerp.financialservice.domain.PaymentRecord;
import com.systeemontwerp.financialservice.domain.PaymentStatus;

@Service
public class FinancialCommandHandler {
	
	private static Logger logger = LoggerFactory.getLogger(FinancialCommandHandler.class);
	private final FinancialService financialService;
	private final FinancialMessageGateway messageGateway;
	
	@Autowired
	private FinancialCommandHandler(FinancialService financialService, FinancialMessageGateway gateway) {
		logger.info("Create the command handler");
		this.financialService = financialService;
		this.messageGateway = gateway;
	}
	//This is the command that handles a create payment message
	@StreamListener(MessagingChannels.CREATE_PAYMENT)
	public void createPayment(PaymentRequest request) {
		logger.info("Received payment request: " + request.toString());
		this.financialService.createPayment(request.getItemIdentifier(), request.getFrom(), request.getTo(), request.getAmount(), request.getMethod());
	}
}
