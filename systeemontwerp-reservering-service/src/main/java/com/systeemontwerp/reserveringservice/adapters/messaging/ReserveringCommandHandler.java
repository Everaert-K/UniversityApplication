package com.systeemontwerp.reserveringservice.adapters.messaging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.systeemontwerp.reserveringservice.domain.Reservering;
import com.systeemontwerp.reserveringservice.domain.ReserveringService;

@Service
public class ReserveringCommandHandler {

	private static Logger logger = LoggerFactory.getLogger(ReserveringCommandHandler.class);
	
	private final ReserveringService reserveringService;

	@Autowired
	public ReserveringCommandHandler(ReserveringService reserveringService) {
		this.reserveringService = reserveringService;
	}
	
	//ask if a specific room is reserved at a specific hour and date, return OK if it is not reserved, NOT OK if it is.
	@StreamListener(Channels.CHECK_HOURS_LOKAAL)
	@SendTo(Channels.GET_HOURS_LOKAAL)
	public CheckHoursLokaalResponse checkHoursLokaal(CheckHoursLokaalRequest request) {
		logger.info("Got hours request for 2: " + request.getLokaalId() + " - " + request.getDate().toString() + " - " + request.getType(), request.getHourIds());
		final List<Reservering> reservering = this.reserveringService.checkReservering(request.getLokaalId(), request.getDate(), request.getTime());
		if (reservering.isEmpty()) {
			//the room is not reserved at the given time and date
			return new CheckHoursLokaalResponse(request.getId(), request.getProfId(), request.getLokaalId(), request.getDate(), request.getTime(), request.getType(), "OK", request.getHourIds());
		} else {
			//the room is already reserved
			return new CheckHoursLokaalResponse(request.getId(), request.getProfId(), request.getLokaalId(), request.getDate(), request.getTime(), request.getType(), "NOT OK", request.getHourIds());
		}
	}
	
	//removes the free hour from the given prof and reverses the room at that time and date
	@StreamListener(Channels.RESERVE_LOKAAL)
	@SendTo(Channels.RESERVED_LOKAAL)
	public ReserveRoomResponse scheduleClass(ReserveRoomRequest request) {
		logger.info("Got reserve request for: " + request.getLokaalId() + " - " + request.getProfId() + " - " + request.getDate() + " - " + request.getTime() + " - " + request.getHourIds() + " - " + request.getType());
		final Reservering reservering = this.reserveringService.reserveRoom(request.getLokaalId(), request.getProfId(),request.getDate(), request.getTime(), request.getType());
		if (reservering != null) {
		     //reserve the hour of the prof
			if (this.reserveringService.reserveHoursProf(request.getHourIds())) {
				return new ReserveRoomResponse(request.getId(), reservering.getProfId(), reservering.getLokaalId(), reservering.getDate(), reservering.getTime(), reservering.getType(), "OK", request.getHourIds());
			}
		} 
		return new ReserveRoomResponse(request.getId(), request.getProfId(), request.getLokaalId(), request.getDate(), request.getTime(), request.getType(), "NOT OK", request.getHourIds());
	}
}
