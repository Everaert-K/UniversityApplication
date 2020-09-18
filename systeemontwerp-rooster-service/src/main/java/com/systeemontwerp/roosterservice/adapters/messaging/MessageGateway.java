package com.systeemontwerp.roosterservice.adapters.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import com.systeemontwerp.roosterservice.adapters.messaging.Channels;

@MessagingGateway
public interface MessageGateway {
	
	@Gateway(requestChannel = Channels.SCHEDULE_CLASS)
	public void scheduleClass(ScheduleClassRequest request);
	
	@Gateway(requestChannel = Channels.SCHEDULE_CLASS_RESULT)
	public void scheduleClassResult(ScheduleClassResponse response);
	
	@Gateway(requestChannel = Channels.SCHEDULE_RESULT)
	public void scheduleResult(ScheduleResponse response);
	
	@Gateway(requestChannel = Channels.GET_HOURS_LOKAAL)
	public void getHoursLokaal(CheckHoursLokaalResponse response);
	
	@Gateway(requestChannel = Channels.CHECK_HOURS_LOKAAL)
	public void checkHoursLokaal(CheckHoursLokaalRequest request);
	
	@Gateway(requestChannel = Channels.RESERVE_LOKAAL)
	public void reserveLokaal(ReserveLokaalRequest request);
	
	@Gateway(requestChannel = Channels.RESERVED_LOKAAL)
	public void reservedLokaal(ReservedLokaalResponse response);
}
