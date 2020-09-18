package com.systeemontwerp.onderzoekservice.adapters.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import com.systeemontwerp.onderzoekservice.domain.OnderzoekService;
import com.systeemontwerp.onderzoekservice.domain.ReserveringType;


@Service
public class OnderzoekCommandHandler {

	private final OnderzoekService onderzoekService;
	private static Logger logger = LoggerFactory.getLogger(OnderzoekService.class);

	@Autowired
	public OnderzoekCommandHandler(OnderzoekService onderzoekService) {
		this.onderzoekService = onderzoekService;
	}
	
	@StreamListener(Channels.GET_HOURS_LOKAAL)
	public void getHoursLokaal(CheckHoursLokaalResponse response) {
		this.logger.info("lokaal" + response.getType() + " " + response.getStatus());
		if(response.getStatus().equals("OK"))
			this.onderzoekService.reserveRoom(response.getId(), response.getProfId(), response.getLokaalId(), response.getDate(), response.getTime(), response.getType(), response.getHourIds());
	}
	
	@StreamListener(Channels.RESERVED_LOKAAL)
	public void reservedLokaal(ReservedLokaalResponse response) {
		this.logger.info("reserved1 " + response.getType() + " " + response.getStatus());
		if (response.getType() == ReserveringType.RESEARCH && response.getStatus().equals("OK")) {
			this.logger.info("reserved2");
			this.onderzoekService.updateResearchDocument(response.getId());
		}
	}
	
}
