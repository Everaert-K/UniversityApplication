package com.systeemontwerp.professorservice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;

import com.systeemontwerp.professorservice.adapters.messaging.Channels;
import com.systeemontwerp.professorservice.domain.Address;
import com.systeemontwerp.professorservice.domain.Hours;
import com.systeemontwerp.professorservice.domain.Professor;
import com.systeemontwerp.professorservice.domain.Time;
import com.systeemontwerp.professorservice.persistence.HoursRepository;
import com.systeemontwerp.professorservice.persistence.ProfessorRepository;

@SpringBootApplication
@EnableBinding(Channels.class)
public class SysteemontwerpProfessorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpProfessorServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner testRepository(ProfessorRepository repository, HoursRepository hoursRepository){
		return (args) -> {
			if (repository.count() == 0) {
				repository.deleteAll();
				hoursRepository.deleteAll();
				
				Address address = new Address("Plein", "13", "", "9500", "Geraardsbergen", 
						"Oost-Vlaanderen", "België");			
				LocalDate date = LocalDate.of(1998, 12, 30);
				Professor prof = new Professor("Joris", "Moreau", address, date, "test@example.com");
				Professor result1 = repository.save(prof);
				
				Address address2 = new Address("Markt", "1", "", "9000", "Gent", 
						"Oost-Vlaanderen", "België");		
				LocalDate date2 = LocalDate.of(1998, 12, 31);
				Professor prof2 = new Professor("Pieter", "Simoens", address2, date2, "test2@example.com");
				Professor result2 = repository.save(prof2);
				
				Address address3 = new Address("Plein", "13", "", "9500", "Geraardsbergen", 
						"Oost-Vlaanderen", "België");			
				LocalDate date3 = LocalDate.of(1998, 12, 30);
				Professor prof3 = new Professor("Eli", "De Poorter", address3, date3, "test@example.com");
				Professor result3 = repository.save(prof3);
				
				Address address4 = new Address("Markt", "1", "", "9000", "Gent", 
						"Oost-Vlaanderen", "België");		
				LocalDate date4 = LocalDate.of(1998, 12, 31);
				Professor prof4 = new Professor("Jan", "Cnops", address4, date4, "test2@example.com");
				Professor result4 = repository.save(prof4);
				
				Address address5 = new Address("Plein", "13", "", "9500", "Geraardsbergen", 
						"Oost-Vlaanderen", "België");			
				LocalDate date5 = LocalDate.of(1998, 12, 30);
				Professor prof5 = new Professor("Bruno", "Volckaert", address5, date5, "test@example.com");
				Professor result5 = repository.save(prof5);
				
				List<Professor> list = new ArrayList();
				list.add(result1);
				list.add(result2);
				list.add(result3);
				list.add(result4);
				list.add(result5);
				
				for (Professor p : list) {
					for(int i = 3; i < 8; i++) {
						Hours hours = new Hours(Time.H8M15, Time.H9M15, 
								LocalDate.of(2020, 2, i), p);
						Hours hours1 = new Hours(Time.H9M15, Time.H10M30, 
								LocalDate.of(2020, 2, i), p);
						Hours hours2 = new Hours(Time.H10M30, Time.H11M30, 
								LocalDate.of(2020, 2, i), p);
						Hours hours3 = new Hours(Time.H11M30, Time.H12M30, 
								LocalDate.of(2020, 2, i), p);
						Hours hours4 = new Hours(Time.H13M30, Time.H14M30, 
								LocalDate.of(2020, 2, i), p);
						Hours hours5 = new Hours(Time.H14M30, Time.H15M45, 
								LocalDate.of(2020, 2, i), p);
						Hours hours6 = new Hours(Time.H15M45, Time.H16M45, 
								LocalDate.of(2020, 2, i), p);
						Hours hours7 = new Hours(Time.H16M45, Time.H18M00, 
								LocalDate.of(2020, 2, i), p);
						Hours hours8 = new Hours(Time.H18M00, Time.H19M00, 
								LocalDate.of(2020, 2, i), p);
						
						hoursRepository.save(hours);
						hoursRepository.save(hours1);
						hoursRepository.save(hours2);
						hoursRepository.save(hours3);
						hoursRepository.save(hours4);
						hoursRepository.save(hours5);
						hoursRepository.save(hours6);
						hoursRepository.save(hours7);
						hoursRepository.save(hours8);
					}
				}
			}	
		};
	}
}
