package com.systeemontwerp.roosterservice.adapters.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Channels {

	static final String SCHEDULE_CLASS = "schedule_class";
	static final String SCHEDULE_CLASS_RESULT = "schedule_class_result";
	static final String SCHEDULE_RESULT = "schedule_result";
	static final String RESERVE_LOKAAL = "reserve_lokaal";
	static final String RESERVED_LOKAAL = "reserved_lokaal";
	static final String CHECK_HOURS_LOKAAL = "check_hours_lokaal";
	static final String GET_HOURS_LOKAAL = "get_hours_lokaal";

	
	@Input(SCHEDULE_CLASS)
	SubscribableChannel scheduleClass();
	
	@Output(SCHEDULE_CLASS_RESULT)
	MessageChannel scheduleClassResult();
	
	@Output(SCHEDULE_RESULT)
	MessageChannel scheduleResult();
	
	@Input(GET_HOURS_LOKAAL)
	SubscribableChannel getHoursLokaal();
	
	@Output(CHECK_HOURS_LOKAAL)
	MessageChannel checkHoursLokaal();
	
	@Output(RESERVE_LOKAAL)
	MessageChannel reserveLokaal();
	
	@Input(RESERVED_LOKAAL)
	MessageChannel reservedLokaal();
}
