package com.systeemontwerp.roosterservice.adapters.messaging;

public class ScheduleClassResponse {

	private Long requestId;
	private String roosterId;
	private String vakId;

	public ScheduleClassResponse(Long requestId, String roosterId, String vakId) {
		this.requestId = requestId;
		this.roosterId = roosterId;
		this.vakId = vakId;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public String getRoosterId() {
		return roosterId;
	}

	public void setRoosterId(String roosterId) {
		this.roosterId = roosterId;
	}

	public String getVakId() {
		return vakId;
	}

	public void setVakId(String vakId) {
		this.vakId = vakId;
	}
	
	
}
