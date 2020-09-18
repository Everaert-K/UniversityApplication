package com.systeemontwerp.reserveringservice.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.reserveringservice.persistence.ReserveringRepository;

@Service
public class ReserveringService {

	private static Logger logger = LoggerFactory.getLogger(ReserveringService.class);
	
	private final ReserveringRepository reserveringRepository;
	
	@Autowired
	public ReserveringService(ReserveringRepository reserveringRepository) {
		this.reserveringRepository = reserveringRepository;
	}

	//returnes a Reservering if the room is already reserved at that time and date
	public List<Reservering> checkReservering(String lokaalId, LocalDate date, Time time) {
		return reserveringRepository.findByLokaalIdAndDateAndTime(lokaalId, date, time);
	}

	public Reservering reserveRoom(String lokaalId, String profId, LocalDate date, Time time, ReserveringType type) {
		List<Reservering> reservering = reserveringRepository.findByLokaalIdAndDateAndTime(lokaalId, date, time);
		if (reservering.isEmpty()) {
			try {
				logger.info("Reserve Room" + lokaalId + " on " + date.toString() + " at " + time.toString());
				Reservering reserve = new Reservering(profId, lokaalId, type, date, time);
				reserveringRepository.save(reserve);
				return reserve;
			} catch (Exception e){
				logger.info("The room is already scheduled at this time andd date");
			}
		} else {
			logger.info("The room is already reserved" + reservering.toString());
		}
		return null;
	}

	public boolean reserveHoursProf(List<String> hourIds) {
		boolean completed = true;
		for (String hour: hourIds) {
			//The http request
			WebClient client = WebClient.create();
			try {
				String hours = client.delete().uri("http://apigateway:8080/api/professors/reserve/" + hour).exchange()
						//Wait for maximum 3seconds
						.timeout(Duration.ofMillis(3000))
						.flatMap(response -> response.bodyToMono(String.class))
						.block();
				this.logger.info("Response from profservice: " + hours);
			} catch(Exception e) {
				completed = false;
				this.logger.info("Could not delete hours " + e.getMessage());
			}
		}
		return completed;
	}
	
	public boolean isValidStudentId(String buyerId) throws ReserveringServiceException{
		WebClient client = WebClient.create();
		try {
			String student = client.get().uri("http://apigateway:8080/api/students/" + buyerId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from studentservice: " + student);
			if(student == null)
				throw new ReserveringServiceException("Invalid student id");
			return student.contains("studentNumber");
		} catch(Exception e) {
			throw new ReserveringServiceException("Could not verify student id");
		}
	}
}
