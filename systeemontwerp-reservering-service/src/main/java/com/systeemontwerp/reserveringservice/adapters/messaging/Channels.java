package com.systeemontwerp.reserveringservice.adapters.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {
	static final String RESERVE_LOKAAL = "reserve_lokaal";
	static final String RESERVED_LOKAAL = "reserved_lokaal";
	static final String GET_HOURS_LOKAAL = "get_hours_lokaal";
	static final String CHECK_HOURS_LOKAAL = "check_hours_lokaal";

	@Input(RESERVE_LOKAAL)
	SubscribableChannel scheduleClass();
	
	@Output(RESERVED_LOKAAL)
	MessageChannel scheduleClassResult();
	
	@Input(CHECK_HOURS_LOKAAL)
	SubscribableChannel checkHoursLokaal();
	
	@Output(GET_HOURS_LOKAAL)
	MessageChannel getHoursLokaal();
}
