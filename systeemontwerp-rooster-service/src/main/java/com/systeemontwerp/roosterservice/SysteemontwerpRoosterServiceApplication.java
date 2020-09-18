package com.systeemontwerp.roosterservice;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.systeemontwerp.roosterservice.domain.Rooster;
import com.systeemontwerp.roosterservice.domain.RoosterService;
import com.systeemontwerp.roosterservice.domain.Time;
import com.systeemontwerp.roosterservice.domain.Vak;
import com.systeemontwerp.roosterservice.domain.VakStatus;
import com.systeemontwerp.roosterservice.persistence.RoosterRepository;
import com.systeemontwerp.roosterservice.persistence.VakRepository;
import com.systeemontwerp.roosterservice.adapters.messaging.Channels;

@SpringBootApplication
@EnableBinding(Channels.class)
public class SysteemontwerpRoosterServiceApplication {
	
	Logger logger = LoggerFactory.getLogger(SysteemontwerpRoosterServiceApplication.class);
	
	//@Autowired
    //private MongoTemplate mongoTemplate;
	//mongoTemplate.dropCollection(Vak.class);
	//mongoTemplate.dropCollection(Rooster.class);

	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpRoosterServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateDatabase(VakRepository repository, RoosterRepository rosterRepository) {
		return (args) -> {
			/* DEPRICATED, NAME IS REPLACED BY ID
			if (rosterRepository.count() == 0) {
				logger.info("Clearing database...");
				rosterRepository.deleteAll();
			}
			
			if (repository.count() == 0) {
				logger.info("Clearing database...");
				repository.deleteAll();
			
				logger.info("populating with new data...");
				Vak v1 = new Vak("Besturingssystemen III", "Joris Moreau", "B2.035", 6);
				repository.save(v1);
				
				Vak v2 = new Vak("Systeemontwerp", "Pieter Simoens", "C2.014", 3);
				repository.save(v2);
				
				Vak v3 = new Vak("Beveiliging van netwerken en computers", "Eli De Poorter", "B4.005", 6);
				repository.save(v3);
				
				Vak v4 = new Vak("Gevorderde algoritmen", "Jan Cnops", "B1.034", 9);
				repository.save(v4);
				
				Vak v5 = new Vak("Gedistribueerde gegevensverwerking", "Bruno Volckaert", "P1.213", 3);
				repository.save(v5);
				
				Vak v6 = new Vak("Computergrafiek ", "Joris Moreau", "B4.003", 3);
				repository.save(v6);
				
				Vak v7 = new Vak("Relationele gegevensbanken", "ID", "B1.034", 6);
				repository.save(v7);
			}
			*/
		};
	}
}
