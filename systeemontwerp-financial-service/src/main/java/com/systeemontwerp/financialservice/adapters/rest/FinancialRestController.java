package com.systeemontwerp.financialservice.adapters.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.systeemontwerp.financialservice.domain.FinancialService;
import com.systeemontwerp.financialservice.domain.PaymentRecord;
import com.systeemontwerp.financialservice.persistence.FinancialRecordRepository;

@RestController
@RequestMapping("/api/financial")
public class FinancialRestController {
	
	private final FinancialRecordRepository financialRepository;
	private final FinancialService financialService;
	
	@Autowired
	private FinancialRestController(FinancialService financialService, FinancialRecordRepository financialRepository) {
		this.financialRepository = financialRepository;
		this.financialService = financialService;
	}
	
	//The general html page
	@GetMapping("")
	public String showIndex() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the Financial page!</h1>");
		site.append("<p>All payment troughout the university-application are handled and listed in this service. "
				+ "This service supports Bancontact, PayPal and Epurse. For Bancontact and PayPal the external apis of "
				+ "these services is used. For testing purposes, those api's were also implemented in this service.</p>");
		site.append("<a href=\"financial/transactions\">Show me the transactions</a>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/transactions")
	public Iterable<PaymentRecord> getAllPaymentRecords(){
		return this.financialRepository.findAll();
	}
	
	@GetMapping("/transactions/{id}")
	public PaymentRecord getRecordWithId(@PathVariable("id") String id){
		return this.financialRepository.findById(id).orElse(new PaymentRecord());
	}
	
	@GetMapping("transactions/from/{id}")
	public Iterable<PaymentRecord> getAllPaymentsFromId(@PathVariable("id") String id){
		return this.financialRepository.findRecordsFrom(id);
	}
	
	@GetMapping("transactions/to/{id}")
	public Iterable<PaymentRecord> getAllPaymentsToId(@PathVariable("id") String id){
		return this.financialRepository.findRecordsTo(id);
	}
	
	@GetMapping("/external_payment_callback")
	public void handleExternalPaymentResult(@RequestParam("transactionId") String id, @RequestParam("status") String status) {
		this.financialService.handleExternalPaymentResult(id, status);
	}
	
	//For testing purposes, bancontact and paypal go trough this service
	//In a real scenario these are in the services of bancontact/paypal applications
	@GetMapping("/bancontact")
	public String handleBancontact(@RequestParam("transactionId") String id, @RequestParam("amount") double amount) {
		//Call handleexternalpaymentresult to simulate successful external payment
		this.handleExternalPaymentResult(id, "ok");
		return id;
	}
	@GetMapping("/paypal")
	public String handlePaypal(@RequestParam("transactionId") String id, @RequestParam("amount") double amount) {
		//Call handleexternalpaymentresult to simulate successful external payment
		this.handleExternalPaymentResult(id, "ok");
		return id;
	}
	
	/*Was used for testing if a server did not respond
	@GetMapping("/waitloop")
	public String waiting() throws Exception{
		Thread.sleep(20000);
		return "done";
	}*/
	
	//Fallback to catch extra exceptions thrown by the application
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}
	

}
