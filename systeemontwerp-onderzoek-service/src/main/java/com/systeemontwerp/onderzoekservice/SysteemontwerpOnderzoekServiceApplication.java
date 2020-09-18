package com.systeemontwerp.onderzoekservice;

import java.time.LocalDate;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.systeemontwerp.onderzoekservice.adapters.messaging.Channels;
import com.systeemontwerp.onderzoekservice.domain.ResearchDocument;
import com.systeemontwerp.onderzoekservice.persistence.ResearchDocumentRepository;

@SpringBootApplication
@EnableBinding(Channels.class)
public class SysteemontwerpOnderzoekServiceApplication {
	
	private static Logger logger = LoggerFactory.getLogger(SysteemontwerpOnderzoekServiceApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpOnderzoekServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateDb(ResearchDocumentRepository repository) {
		
		return (args) -> {
			/* DEPRICATED, uses ids instead of name now
			if (repository.count() == 0) {
				logger.info("Emptying database...");
				repository.deleteAll();
				
				logger.info("Start populating...");
				ArrayList<String> profIds = new ArrayList<String>();
				profIds.add("karel");
				ResearchDocument r1 = new ResearchDocument("onderzoek 1", LocalDate.now(), profIds, 12, "B2.014", 1);
				repository.save(r1);
			}
			*/
		};
	}

}
