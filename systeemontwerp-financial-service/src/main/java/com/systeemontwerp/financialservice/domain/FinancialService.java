package com.systeemontwerp.financialservice.domain;

import java.time.Duration;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.financialservice.adapters.messaging.FinancialMessageGateway;
import com.systeemontwerp.financialservice.adapters.messaging.PaymentResponse;
import com.systeemontwerp.financialservice.persistence.FinancialRecordRepository;


@Service
public class FinancialService {
	private static Logger logger = LoggerFactory.getLogger(FinancialService.class);
	private final FinancialRecordRepository recordRepository;
	private final FinancialMessageGateway messageGateway;
	
	private HashMap<String, PaymentRecord> handlingRecords;
	private HashMap<DeferredResult<String>, String> reverseHandlingRecords;
	
	private HashMap<PaymentMethod, String> paymentUriMap;
	
	@Autowired
	public FinancialService(FinancialRecordRepository recordRepository, FinancialMessageGateway messageGateway) {
		this.recordRepository = recordRepository;
		this.messageGateway = messageGateway;
		this.handlingRecords = new HashMap<>();
		this.reverseHandlingRecords = new HashMap<>();
		this.paymentUriMap = new HashMap<>();
		
		//Now, external payments are called on the api-gateway, in real life, these should be
		//the urls of het external payment companies
		this.paymentUriMap.put(PaymentMethod.BANCONTACT, "http://apigateway:8080/api/financial/bancontact?transactionId=%s&amount=%.2f");
		this.paymentUriMap.put(PaymentMethod.PAYPAL, "http://apigateway:8080/api/financial/paypal?transactionId=%s&amount=%.2f");
	}
	
	public void handleExternalPaymentResult(String transactionId, String status) {
		PaymentRecord record = this.recordRepository.findById(transactionId).orElse(new PaymentRecord());
		if(record.getId() !=  null) {
			//Record was found
			PaymentResponse response =  new PaymentResponse(record.getId(), record.getItemIdentifier(), record.getFromId(), record.getToId(), record.getAmount(), record.getStatus());
			//Test if external payment was a success
			if(status.equals("ok")) {
				record.setStatus(PaymentStatus.SUCCESS);
				response.setStatus(PaymentStatus.SUCCESS);
				this.messageGateway.emitPaymentOk(response);
			} else {
				record.setStatus(PaymentStatus.FAILED);
				response.setStatus(PaymentStatus.FAILED);
				this.messageGateway.emitPaymentFailed(response);
			}
			//Update the database
			this.recordRepository.save(record);
		} else {
			//Invalid payment received, do nothing
		}
	}
	
	private void handleExternalPaymentFailed(String trxnId, DeferredResult<String> res) {
		this.handlingRecords.get(trxnId).setStatus(PaymentStatus.FAILED);
		PaymentRecord saved = this.recordRepository.save(this.handlingRecords.get(trxnId));
		this.handlingRecords.remove(trxnId);
		FinancialService.logger.info("External payment with id:" + trxnId + " failed");
		this.messageGateway.emitPaymentFailed(new PaymentResponse(
				saved.getId(), saved.getItemIdentifier(), saved.getFromId(), saved.getToId(), saved.getAmount(), saved.getStatus())
				);
		//Cleanup
		if(res != null)
			this.reverseHandlingRecords.remove(res);
	}
	
	private void handleExternalPaymentSuccess(String trxnId, DeferredResult<String> res) {
		this.handlingRecords.get(trxnId).setStatus(PaymentStatus.SUCCESS);
		PaymentRecord saved = this.recordRepository.save(this.handlingRecords.get(trxnId));
		this.handlingRecords.remove(trxnId);
		FinancialService.logger.info("Payment with id:" + trxnId + " was a success");
		this.logger.info("Sending " + saved.toString() + " in messagingchannel");
		this.messageGateway.emitPaymentOk(new PaymentResponse(
				saved.getId(), new String(saved.getItemIdentifier()), saved.getFromId(), saved.getToId(), saved.getAmount(), saved.getStatus())
				);
		//Cleanup
		if(res != null)
			this.reverseHandlingRecords.remove(res);
	}
	
	private DeferredResult<String> requestExternalPayment(String transactionid, PaymentMethod method, double amount){
		//After five seconds of no answer, the external payment failed
		DeferredResult<String> request = new DeferredResult<>(5000l);
		//Uri's for the external payment services
		final String payment_uri = String.format(this.paymentUriMap.get(method), transactionid, amount);
		//The http request
		WebClient client = WebClient.create();
		//Add a small delay so all callbacks are set
		client.get().uri(payment_uri).exchange().delayElement(Duration.ofMillis(50))
			.timeout(Duration.ofMillis(5000))
			.doOnSuccess((succ) -> {this.logger.info("Successful external payment response");})
			.flatMap(resp -> resp.bodyToMono(String.class))
			.subscribe(
					(result) -> request.setResult(result),
					(error) -> request.setErrorResult("ERROR: Payment error: " + error.getMessage()),
					() -> request.setErrorResult("ERROR: Payment error")
					);

		return request;
	}
	
	public PaymentRecord createPayment(String itemIdentifier, String from, String to, double amount, PaymentMethod method) {
		final PaymentRecord  record = this.recordRepository.save(new PaymentRecord(itemIdentifier, from, to, amount, method));
		this.handlingRecords.put(record.getId(), record);
		
		//The only internal payment for now
		if(method == PaymentMethod.EPURSE) {
			//Naming should be handleInternalPaymentSuccess but behaves the same as handleExternalPaymentSuccess in this application
			this.logger.info("EPURSE: " + record.toString());
			this.handleExternalPaymentSuccess(record.getId(), null);
		} else {
			//The others are external payments
			DeferredResult<String> res = this.requestExternalPayment(record.getId(), method, amount);
			this.logger.info("EXTERNAL: " + record.toString());
			this.reverseHandlingRecords.put(res, record.getId());
			//this.handlingRecordsResults.put(record.getId(), res);
			res.setResultHandler((r) -> {
				if(this.handlingRecords.containsKey(r)) {
					this.handleExternalPaymentSuccess((String)r, res);
				} else { 
					FinancialService.logger.error("Invalid response: " + r);
					//Fetch the transactionid from the failed deferredresult
					this.handleExternalPaymentFailed(this.reverseHandlingRecords.get(res), res);
				}
			});
			res.onError((err) -> {
				//Fetch the transactionid from the failed deferredresult
				FinancialService.logger.info(err.getMessage());
				this.handleExternalPaymentFailed(this.reverseHandlingRecords.get(res), res);
			});
			res.onTimeout(() -> res.setErrorResult("ERROR: Payment timeout"));
		}
		return record;
	}
}
