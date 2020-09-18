package com.systeemontwerp.studentservice.adapters.messaging;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.studentservice.domain.StudentService;

@Service
public class StudentEventHandler {
	
	private final StudentService studentService;
	private final StudentMessageGateway messageGateway;
	private final Logger logger = Logger.getLogger(StudentEventHandler.class);
	
	@Autowired
	public StudentEventHandler(StudentService studentService, StudentMessageGateway messageGateway) {
		this.studentService = studentService;
		this.messageGateway = messageGateway;
	}
	
	@StreamListener(Channels.PAYMENT_OK)
	public void handlePaymentOk(PaymentResponse response) {
		this.logger.info("Payment for " + response.getItemIdentifier() + " succeeded");
		this.studentService.paymentSuccessful(response.getItemIdentifier());
	}
	
	@StreamListener(Channels.PAYMENT_FAILED)
	public void handlePaymentFailed(PaymentResponse response) {
		this.logger.info("Payment for " + response.getItemIdentifier() + " failed");			
		this.studentService.paymentFailed(response.getItemIdentifier());
	}
	
	@StreamListener(Channels.STUDENT_DISCOUNT_REQUEST)
	public void handleDiscountRequest(DiscountApplication application) {
		boolean studentExists = this.studentService.doesStudentExist(application.getNummer());
		DiscountResponse response = new DiscountResponse(application.getId(), studentExists);
		messageGateway.emitDiscountResponse(response);
	}
	
}
