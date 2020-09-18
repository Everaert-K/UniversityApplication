package com.systeemontwerp.onderzoekservice.adapters.messaging;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface MessageGateway {

	@Gateway(requestChannel = Channels.GET_HOURS_LOKAAL)
	public void getHoursLokaal(CheckHoursLokaalResponse response);
	
	@Gateway(requestChannel = Channels.CHECK_HOURS_LOKAAL)
	public void checkHoursLokaal(CheckHoursLokaalRequest request);
	
	@Gateway(requestChannel = Channels.RESERVE_LOKAAL)
	public void reserveLokaal(ReserveLokaalRequest request);
	
	@Gateway(requestChannel = Channels.RESERVED_LOKAAL)
	public void reservedLokaal(ReservedLokaalResponse response);
}
