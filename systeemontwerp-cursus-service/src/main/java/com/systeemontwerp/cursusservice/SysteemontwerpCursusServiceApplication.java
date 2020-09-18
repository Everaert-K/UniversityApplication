package com.systeemontwerp.cursusservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.systeemontwerp.cursusservice.adapteres.messaging.MessagingChannels;
import com.systeemontwerp.cursusservice.domain.LiteratureItem;
import com.systeemontwerp.cursusservice.domain.LiteratureItemReservation;
import com.systeemontwerp.cursusservice.persistence.LiteratureItemRepository;
import com.systeemontwerp.cursusservice.persistence.ReservationRepository;

@SpringBootApplication
@EnableBinding(MessagingChannels.class)
public class SysteemontwerpCursusServiceApplication {
	
	private static Logger logger = LoggerFactory.getLogger(SysteemontwerpCursusServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpCursusServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateLiteratureItemDatabase(LiteratureItemRepository repo) {
		return (args) -> {
			//If we have an empty database, populate it
			if(repo.count() == 0) {
				this.logger.info("Adding some books to the library");
				LiteratureItem cursus1 = new LiteratureItem("Systeemontwerp", "Pieter Simoens", 9.99, "BExxpsimoensxx", 20, false, "content".getBytes());
				LiteratureItem cursus2 = new LiteratureItem("BesturingssystemenIII", "Joris Moreau", 9.99, "BExxjmoreauxx", 20, false, "content".getBytes());
				LiteratureItem book1 = new LiteratureItem("Mein Lampf", "Thomas Edison", 10.99, "BExxxbookstorexxx", 10,true, new byte[0]);
				LiteratureItem book2 = new LiteratureItem("Mechanica", "Packt", 11.99, "BExxxbookstorexxx", 10, true, new byte[0]);
				repo.save(cursus1);
				repo.save(cursus2);
				repo.save(book1);
				repo.save(book2);
			}
			
			this.logger.info("Amount of literatureitems in database: " + repo.findAll().size());
		};
	}

}
