package com.systeemontwerp.roosterservice.adapters.messaging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.roosterservice.domain.ReserveringType;
import com.systeemontwerp.roosterservice.domain.RoosterService;

@Service
public class RoosterCommandHandler {

	private static Logger logger = LoggerFactory.getLogger(RoosterCommandHandler.class);

	private final RoosterService roosterService;

	@Autowired
	public RoosterCommandHandler(RoosterService roosterService) {
		this.roosterService = roosterService;
	}
	/* not used
	@StreamListener(Channels.SCHEDULE_CLASS)
	@SendTo(Channels.SCHEDULE_CLASS_RESULT)
	public ScheduleClassResponse scheduleClass(ScheduleClassRequest request) {

		logger.info("Got request for : " + request.getVakId() + " - " + request.getDay() + " - " + request.getTime());
		final boolean rooster = this.roosterService.scheduleClass(request.getVakId(), request.getDay(), request.getTime());
		if (rooster) {
			return new ScheduleClassResponse(request.getId(), "OK", request.getVakId());
		} else {
			return new ScheduleClassResponse(request.getId(), "NOT OK", request.getVakId());
		}
	}
	*/
	
	/* replaced
	@StreamListener(Channels.GET_HOURS_PROF)
	public void getHoursProf(CheckHoursProfResponse response) {
		logger.info("Got hours prof response for : " + response.getLokaalId() + " - " + response.getStatus());
		if(response.getStatus().equals("OK"))
			this.roosterService.checkHoursLokaal(response.getVakId(), response.getProfId(), response.getLokaalId(), response.getDate(), response.getTime(), response.getType());
	}
	*/
	
	@StreamListener(Channels.GET_HOURS_LOKAAL)
	public void getHoursLokaal(CheckHoursLokaalResponse response) {
		logger.info("Got hours response for: " + response.getLokaalId() + " - " + response.getStatus() + response.getType());
		if(response.getStatus().equals("OK"))
			this.roosterService.reserveRoom(response.getId(), response.getProfId(), response.getLokaalId(), response.getDate(), response.getTime(), response.getType(), response.getHourIds());
	}
	
	@StreamListener(Channels.RESERVED_LOKAAL)
	public void reservedLokaal(ReservedLokaalResponse response) {
		try {
			logger.info("Got reserved response for: " + response.getLokaalId() + " "+ response.getDate() + " "+ response.getTime());
			if (response.getType() == ReserveringType.CLASS && response.getStatus().equals("OK")) {
				//update the reserved hours of the class
				this.roosterService.updateVak(response.getId());
				//add it to the rooster
				this.roosterService.updateRooster(response.getId(), response.getLokaalId(), response.getDate(), response.getTime());
			}
		} catch (Exception e) {
			logger.info("Exception caught in RoosterCommandHandler RESERVED_LOKAAL");
		}
	}
}
