package com.systeemontwerp.onderzoekservice.domain;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.reactive.function.client.WebClient;

import com.systeemontwerp.onderzoekservice.adapters.messaging.CheckHoursLokaalRequest;
import com.systeemontwerp.onderzoekservice.adapters.messaging.MessageGateway;
import com.systeemontwerp.onderzoekservice.adapters.messaging.ReserveLokaalRequest;
import com.systeemontwerp.onderzoekservice.persistence.ResearchDocumentRepository;

@Service
public class OnderzoekService {
	
	private static Logger logger = LoggerFactory.getLogger(OnderzoekService.class);
	private final ResearchDocumentRepository repository;
	private final MessageGateway messageGateway;
	
	private HashMap<String, ResearchDocument> handlingDocuments;
	private HashMap<DeferredResult<String>, String> reverseHandlingDocuments;
	
	@Autowired
	public OnderzoekService(ResearchDocumentRepository repository, MessageGateway messageGateway) {
		this.repository = repository;
		this.messageGateway = messageGateway;
		this.handlingDocuments = new HashMap<>();
		this.reverseHandlingDocuments = new HashMap();
	}
	
	public void handleExternalApproval(String documentId, String status) {
		ResearchDocument document = this.repository.findById(documentId).orElse(null);
		if(document == null) {
			// then document not in db so you can just ignore
		}
		else {
			if(status.equals("ok")) {
				document.setStatus(Status.APROVED);
				// fetch available hours of professors with professorService
				// fetch available hours of rooms with reserveringsService
				// reserving a room at a specific hour requires manual intervention 
			}
			else {
				document.setStatus(Status.REJECTED);
				//this.controller.putResearchDocument(document);
				putResearchDocument(document);
			}
		}
	}
	
	private void putResearchDocument(ResearchDocument document) {
		this.repository.deleteById(document.getId());
		this.repository.save(document);		
	}
		
	/*
	private DeferredResult<String> requestAvailableHoursProf(String profId){
		//After five seconds of no answer, the external payment failed
		DeferredResult<String> request = new DeferredResult<>(5000l);
		final String prof_uri = "http://apigateway:8080/api/professors/hours/"+profId; 
		//The http request
		WebClient client = WebClient.create();
		//Add a small delay so all callbacks are set
		client.get().uri(prof_uri).exchange().delayElement(Duration.ofMillis(50))
			.timeout(Duration.ofMillis(5000))
			.doOnSuccess((succ) -> {this.logger.info("Successful prof hours response");})
			.flatMap(resp -> resp.bodyToMono(String.class))
			.subscribe(
					(result) -> request.setResult(result),
					(error) -> request.setErrorResult("ERROR: prof hour error: " + error.getMessage()),
					() -> request.setErrorResult("ERROR: prof hour error")
					);

		return request;
	}
	*/

	
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
				if (hours.contains("id") && hours.indexOf("id\":\"") >= 0 && hours.indexOf("\"}") >= 0) {
					String id = hours.substring(hours.indexOf("id\":\""),hours.indexOf("\"}"));
					return id;
				}
			}
		} catch(Exception e) {
			this.logger.info("Could not verify hours " + e.getMessage());
		}
		return "0";
	}
		
	
	public ResearchDocument createDocument(String description, LocalDate terminationDate, ArrayList<String> profIds, double estimatedPrice, int neededHours, String lokaalId) {
		ResearchDocument document = new ResearchDocument(description, terminationDate, profIds, estimatedPrice, lokaalId, neededHours);
		document.setStatus(Status.APROVED); // this normally has to be approved by government but we will simulate here that everything gets approved
		this.repository.save(document);
		return document;
	}

	
	public Boolean reserveRoom(String documentId, LocalDate date, Time time) {
		ResearchDocument researchDocument = this.repository.findById(documentId).orElse(null);
		this.logger.info("reserve");
		if (researchDocument != null) {
			if (researchDocument.getScheduledHours() < researchDocument.getNeededHours()) {
				this.checkHoursProf1(researchDocument.getId(), researchDocument.getProfIds().get(0), researchDocument.getLokaalId(), date, time, ReserveringType.RESEARCH);
				return true;
			}	
		}
		return false;
	}
	

	public void checkHoursProf1(String documentId, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type) {
		ResearchDocument researchDocument = this.repository.findById(documentId).orElse(null);
		this.logger.info("prof1 " + type);
		if (researchDocument != null) {
			if (!profId.isEmpty()) {
				String response = this.requestAvailableHoursProf(profId, time, date);
				if(!response.equals("0")) {
					List<String> hourIds = new ArrayList<String>();
					hourIds.add(response);
					this.checkHoursProf2(documentId, lokaalId, date, time, type, hourIds);
				}
			}
		}
	}
	

	public void checkHoursProf2(String documentId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		ResearchDocument researchDocument = this.repository.findById(documentId).orElse(null);
		this.logger.info("prof2 " + type);
		if (researchDocument != null && researchDocument.getProfIds().size() >= 2) {
			String profId = researchDocument.getProfIds().get(1);
			if (!profId.isEmpty()) {
				String response = this.requestAvailableHoursProf(profId, time, date);
				if(!response.equals("0")) {
					hourIds.add(response);
					this.checkHoursProf3(documentId, lokaalId, date, time, type, hourIds);
				}
			} else {
				checkHoursLokaal(documentId, researchDocument.getProfIds().get(0), lokaalId, date, time, type, hourIds);
			}
		} else {
			checkHoursLokaal(documentId, researchDocument.getProfIds().get(0), lokaalId, date, time, type, hourIds);
		}
	}
	
	public void checkHoursProf3(String documentId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		ResearchDocument researchDocument = this.repository.findById(documentId).orElse(null);
		this.logger.info("prof3 " + type);
		if (researchDocument != null && researchDocument.getProfIds().size() >= 3) {
			String profId = researchDocument.getProfIds().get(2);
			if (!profId.isEmpty()) {
				String response = this.requestAvailableHoursProf(profId, time, date);
				if(!response.equals("0")) {
					hourIds.add(response);
					checkHoursLokaal(documentId, researchDocument.getProfIds().get(0), lokaalId, date, time, type, hourIds);
				}
			} else {
				checkHoursLokaal(documentId, researchDocument.getProfIds().get(0), lokaalId, date, time, type, hourIds);
			}
		} else {
			checkHoursLokaal(documentId, researchDocument.getProfIds().get(0), lokaalId, date, time, type, hourIds);
		}
	}
	
	public void checkHoursLokaal(String documentId, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		CheckHoursLokaalRequest request = new CheckHoursLokaalRequest(documentId, profId, lokaalId, date, time, type, hourIds);
		this.logger.info("lokaal service " + type);
		this.messageGateway.checkHoursLokaal(request);	
	}
	
	public void reserveRoom(String vakId, String profId, String lokaalId, LocalDate date, Time time, ReserveringType type, List<String> hourIds) {
		this.logger.info("reserve room service " + type);
		ReserveLokaalRequest request = new ReserveLokaalRequest(vakId, profId, lokaalId, date, time, type, hourIds);
		messageGateway.reserveLokaal(request);
	}

	public void updateResearchDocument(String id) {
		ResearchDocument rd = this.repository.findById(id).orElse(null);
		if (rd != null) {
			this.logger.info("update research document");
			rd.setScheduledHours(rd.getScheduledHours()+1);
			if (rd.getScheduledHours() == rd.getNeededHours()) {
				rd.setReserveStatus(ReserveStatus.RESERVED);
			}
			this.repository.save(rd);
		}
	}
	
	public boolean isValidProfId(String profId) throws OnderzoekServiceException{
		WebClient client = WebClient.create();
		try {
			String prof = client.get().uri("http://apigateway:8080/api/professors/" + profId).exchange()
					//Wait for maximum 3seconds
					.timeout(Duration.ofMillis(3000))
					.flatMap(response -> response.bodyToMono(String.class))
					.block();
			this.logger.info("Response from profservice: " + prof);
			if(prof == null) {
				throw new OnderzoekServiceException("Invalid profid");
			}
			return prof.contains("employeeNumber");
		} catch(Exception e) {
			throw new OnderzoekServiceException("Could not verify prof id");
		}
	}
	
	
}












