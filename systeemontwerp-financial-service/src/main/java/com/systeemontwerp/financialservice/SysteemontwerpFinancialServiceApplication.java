package com.systeemontwerp.financialservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.systeemontwerp.financialservice.adapters.messaging.FinancialMessageGateway;
import com.systeemontwerp.financialservice.adapters.messaging.MessagingChannels;
import com.systeemontwerp.financialservice.adapters.messaging.PaymentRequest;
import com.systeemontwerp.financialservice.domain.PaymentMethod;
import com.systeemontwerp.financialservice.domain.PaymentRecord;
import com.systeemontwerp.financialservice.domain.PaymentStatus;
import com.systeemontwerp.financialservice.persistence.FinancialRecordRepository;

@SpringBootApplication
@EnableBinding(MessagingChannels.class)
public class SysteemontwerpFinancialServiceApplication {

	Logger logger = LoggerFactory.getLogger(SysteemontwerpFinancialServiceApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpFinancialServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateDb(FinancialRecordRepository repository) {
		
		return (args) -> {
			//If we have an empty database, populate it
			if(repository.count() == 0) {
				logger.info("Populating the database...");
				PaymentRecord r = new PaymentRecord("testpayment", "sender", "receiver", 99.99, PaymentMethod.BANCONTACT);
				repository.save(r);
			}
			logger.info("Amount of payments in the database: " + repository.count());
		};
	}
	
	/*
	@Bean
	public CommandLineRunner testRepositoryMethods(FinancialRecordRepository repository) {
		return (args) -> {
			logger.info("Showing all payments:");
			repository.findAll().forEach((record) -> logger.info(record.toString()));
			
			logger.info("Showing all payments from a , should be 1:");
			repository.findRecordsFrom("a").forEach((record) -> logger.info(record.toString()));
			
			logger.info("Showing all payments to b, should be 4:");
			repository.findRecordsTo("b").forEach((record) -> logger.info(record.toString()));
			
			logger.info("Showing all payments that have failed, should be 2:");
			repository.findPaymentsWithStatus(PaymentStatus.FAILED).forEach((record) -> logger.info(record.toString()));
		};
	}*/
	/*
	@Bean
	public CommandLineRunner testMessagingGateway(FinancialRecordRepository repo, FinancialMessageGateway gateway) {
		return (args) -> {
			repo.deleteAll();
			PaymentRequest r = new PaymentRequest("aaa", "a", "b", 0.1, PaymentMethod.PAYPAL);
			PaymentRequest r2 = new PaymentRequest("bbb", "a", "b", 0.1, PaymentMethod.BANCONTACT);
			PaymentRequest r3 = new PaymentRequest("ccc", "a", "b", 0.1, PaymentMethod.EPURSE);
			gateway.createPayment(r);
			gateway.createPayment(r2);
			gateway.createPayment(r3);
		};
	}*/

}
