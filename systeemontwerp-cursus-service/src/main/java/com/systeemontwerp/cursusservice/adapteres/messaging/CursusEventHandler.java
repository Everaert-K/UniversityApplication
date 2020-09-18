package com.systeemontwerp.cursusservice.adapteres.messaging;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.cursusservice.domain.CursusService;

@Service
public class CursusEventHandler {

	
	private final CursusService cursusService;
	private final Logger logger = Logger.getLogger(CursusEventHandler.class);
	
	@Autowired
	public CursusEventHandler(CursusService cursusService) {
		this.cursusService = cursusService;
	}
	
	@StreamListener(MessagingChannels.PAYMENT_OK)
	public void handlePaymentOk(PaymentResponse response) {
		//Test if the payment was a schoolpayout or not
		this.logger.info("Payment for " + response.getItemIdentifier() + " succeeded");
		if(!response.getItemIdentifier().equals("payout")) {
			this.cursusService.buyPaymentSuccessful(response.getItemIdentifier());
		}
	}
	
	@StreamListener(MessagingChannels.PAYMENT_FAILED)
	public void handlePaymentFailed(PaymentResponse response) {
		//If payment from user failed, book can not be bought
		//If payment from scoop failed, add to debt
		if(!response.getItemIdentifier().equals("payout")) {
			this.logger.info("Payment from buyer not successful");			
			this.cursusService.buyPaymentFailed(response.getItemIdentifier());
		} else {
			this.logger.info("Payment from school to prof/bookstore not successfull");
			this.cursusService.payoutFailed(response.getTo(), response.getAmount());
		}
	}
}
