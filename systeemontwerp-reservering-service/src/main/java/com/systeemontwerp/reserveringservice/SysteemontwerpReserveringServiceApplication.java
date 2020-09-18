package com.systeemontwerp.reserveringservice;

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

import com.systeemontwerp.reserveringservice.adapters.messaging.Channels;
import com.systeemontwerp.reserveringservice.domain.Reservering;
import com.systeemontwerp.reserveringservice.domain.ReserveringType;
import com.systeemontwerp.reserveringservice.domain.Time;
import com.systeemontwerp.reserveringservice.persistence.ReserveringRepository;

@SpringBootApplication
@EnableBinding(Channels.class)
public class SysteemontwerpReserveringServiceApplication {

	Logger logger = LoggerFactory.getLogger(SysteemontwerpReserveringServiceApplication.class);
	
	//@Autowired
    //private MongoTemplate mongoTemplate;
	//mongoTemplate.dropCollection(Reservering.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpReserveringServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateDatabase(ReserveringRepository repository) {
		return (args) -> {
			/* DEPRICATED, name is replaced by id
			if (repository.count() == 0) {
				logger.info("Clearing database...");
				repository.deleteAll();
				
				logger.info("populating with new data...");
				Reservering v1 = new Reservering("Robbe De Sutter", "B2.035", ReserveringType.ACTIVITY, LocalDate.now(), Time.H10M30);
				repository.save(v1);
				
				Reservering v2 = new Reservering("Vince Naessens", "B2.033", ReserveringType.ACTIVITY, LocalDate.now(), Time.H10M30);
				repository.save(v2);
			}
			*/
		};
	}
}
