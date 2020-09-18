package com.systeemontwerp.roosterservice.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.roosterservice.adapters.messaging.CheckHoursLokaalRequest;
import com.systeemontwerp.roosterservice.adapters.messaging.MessageGateway;
import com.systeemontwerp.roosterservice.adapters.messaging.ReserveLokaalRequest;
import com.systeemontwerp.roosterservice.persistence.RoosterRepository;
import com.systeemontwerp.roosterservice.persistence.VakRepository;

@Service
public class RoosterService {

	private static Logger logger = LoggerFactory.getLogger(RoosterService.class);
	
	private final VakRepository vakRepository;
	private final RoosterRepository roosterRepository;
	private final MessageGateway messageGateway;
	
	@Autowired
	public RoosterService(VakRepository vakRepository, RoosterRepository roosterRepository, MessageGateway messageGateway) {
		this.vakRepository = vakRepository;
		this.roosterRepository = roosterRepository;
		this.messageGateway = messageGateway;
	}
	//tries to schedule a class
	public boolean scheduleClass(String vakId, LocalDate date, Time time) {
		Vak vak = vakRepository.findById(vakId).orElse(null);
		if (vak != null) {
			//only if the class exists and still has hours left that needs to be scheduled
			if (vak.getHoursRostered() < vak.getHours()) {
				//check if the prof is available at the chosen time and date
				 boolean ok = this.checkHoursProf(vak.getId(), vak.getProfId(), vak.getLokaalId(), date, time, ReserveringType.CLASS);
				 if (ok) {
					 return true;
				 }
			}	
		}
		return false;
	}
	
	public void checkHoursLokaal(String vakId, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		//send request to the reservering service to ask if the room is available
		CheckHoursLokaalRequest request = new CheckHoursLokaalRequest(vakId, profId, lokaalId, date, time, type, hourIds);
		this.logger.info("check hours lokaal 1 " + type);
		this.messageGateway.checkHoursLokaal(request);
	}
	
	private String requestAvailableHoursProf(String profId, Time time, LocalDate date){
		//After five seconds of no answer, the external payment failed
		DeferredResult<List<Hours>> request = new DeferredResult<>(5000l);
		//The http request
		WebClient client = WebClient.create();
		try {
			String hours = client.get().uri("http://apigateway:8080/api/professors/hours/" + profId + "/" + time + "/" + date).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from profservice: " + hours);
			if(hours == null) {
				this.logger.info("Hour not available");
			} else {
				String id = hours.substring(hours.indexOf("id\":\"")+5,hours.indexOf("\"}"));
				this.logger.info("id " + id);
				return id;
			}
		} catch(Exception e) {
			this.logger.info("Could not verify hours " + e.getMessage());
		}
		return "0";
	}
	
	public boolean checkHoursProf(String vakId, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type) {
		//CheckHoursProfRequest request = new CheckHoursProfRequest(vakId, profId, lokaalId, date, time, type, 1);
		this.logger.info("prof " + type);
		String response = this.requestAvailableHoursProf(profId, time, date);
		if(!response.equals("0")) {
			List<String> hourIds = new ArrayList<String>();
			hourIds.add(response);
			this.checkHoursLokaal(vakId, profId, lokaalId, date, time, type, hourIds);
			return true;
		}
		return false;
	}
	
	public void reserveRoom(String vakId, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		//send request to the reservering service to reserve the room
		ReserveLokaalRequest request = new ReserveLokaalRequest(vakId, profId, lokaalId, date, time, type, hourIds);
		this.logger.info("reserve room 4" + type);
		messageGateway.reserveLokaal(request);
	}

	public Optional<Vak> findVakById(String id) {
		return vakRepository.findById(id);
	}

	public void updateVak(String vakId) {
		Vak vak = this.vakRepository.findById(vakId).orElse(null);
		if (vak != null) {
			vak.setHoursRostered(vak.getHoursRostered()+1);
			if (vak.getHoursRostered() == vak.getHours()) {
				vak.setStatus(VakStatus.CHEDULED);
			}
			vakRepository.save(vak);
			/* unused
			if (this.vakRepository.findByStatus(VakStatus.UNCHEDULED).isEmpty()) {
				//every Vak has been scheduled
				ScheduleResponse response = new ScheduleResponse();
				this.messageGateway.scheduleResult(response);
			}
			*/
		}
	}

	public void saveRooster(Rooster rooster) {
		roosterRepository.save(rooster);
	}

	public void updateRooster(String vakId, String lokaalId, LocalDate date, Time time) {
		Rooster r = new Rooster(vakId, lokaalId, time, date);
		this.roosterRepository.save(r);
	}
	
	public boolean isValidProfId(String profId) throws RoosterServiceException{
		WebClient client = WebClient.create();
		try {
			String prof = client.get().uri("http://apigateway:8080/api/professors/" + profId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from profservice: " + prof);
			if(prof == null) {
				throw new RoosterServiceException("Invalid profid");
			}
			return prof.contains("employeeNumber");
		} catch(Exception e) {
			throw new RoosterServiceException("Could not verify prof id");
		}
	}
}
