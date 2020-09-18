package com.systeemontwerp.studentservice;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.systeemontwerp.studentservice.adapters.messaging.Channels;
import com.systeemontwerp.studentservice.domain.Address;
import com.systeemontwerp.studentservice.domain.Student;
import com.systeemontwerp.studentservice.persistence.StudentRepository;

@SpringBootApplication
@EnableBinding(Channels.class)
public class SysteemontwerpStudentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpStudentServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner testRepository(StudentRepository repository){
		return (args) -> {
			if (repository.count() == 0) {
				Address address = new Address("Plein", "11", "", "9500", "Geraardsbergen", 
						"Oost-Vlaanderen", "België");			
				LocalDate date = LocalDate.of(1998, 12, 30);
				Student student = new Student("Dylan", "De Roe", address, date, "test@example.com");
				repository.save(student);
				
				Address address2 = new Address("Markt", "1", "", "9000", "Gent", 
						"Oost-Vlaanderen", "België");		
				LocalDate date2 = LocalDate.of(1998, 12, 31);
				Student student2 = new Student("Robbe", "De Sutter", address2, date2, "test2@example.com");
				repository.save(student2);
			}
		};
	}
 
}
