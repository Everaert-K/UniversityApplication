package com.systeemontwerp.onderzoekservice.adapters.rest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.systeemontwerp.onderzoekservice.domain.OnderzoekService;
import com.systeemontwerp.onderzoekservice.domain.OnderzoekServiceException;
import com.systeemontwerp.onderzoekservice.domain.ResearchDocument;
import com.systeemontwerp.onderzoekservice.domain.ReserveStatus;
import com.systeemontwerp.onderzoekservice.domain.Status;
import com.systeemontwerp.onderzoekservice.domain.Time;
import com.systeemontwerp.onderzoekservice.persistence.ResearchDocumentRepository;


@RestController
@RequestMapping(path="/api/research")
@CrossOrigin(origins="*")
public class ResearchDocumentRestController {
	private ResearchDocumentRepository repo;
	private final OnderzoekService onderzoekService;
	
	@Autowired
	public ResearchDocumentRestController(ResearchDocumentRepository repo, OnderzoekService onderzoekService) {
		this.repo = repo;
		this.onderzoekService = onderzoekService;
	}
	
	//The general html page
	@GetMapping("")
	public String showIndex() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the ResearchPage!</h1>");
		site.append("<p>In this service yuo can add research documents and schedule a room for the research. A research document is only added to the database after every professor is verified through the Professor Service.</p>");
		site.append("<p>A professor can use this service to reserve a room for a research.</p>");
		site.append("<p>If all the professors and the room are available the research is scheduled at the given time and date, the room is reserved in the Reservering Service and the Reservering service will book the professors their hours.</p>");
		site.append("<h2>View information Research Documents</h2>");
		site.append("<a href=\"/api/research/show\">Show me the research documents</a><br>");
		site.append("<p>To view specific document -> get /api/research/{document-id}</p>");
		site.append("<a href=\"/api/research/add\">Add a research document</a><br>");
		site.append("<a href=\"/api/research/reserve\">Reserve a room for the research<br>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/show")
	public Iterable<ResearchDocument> showResearchDocuments(){
		/* DEPRICATED site
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<ul>");
		for(ResearchDocument document : this.repo.findAll()){
			site.append("<li>"+document.toString()+"</li>");
			}
		site.append("</ul>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
		*/
		return this.repo.findAll();
	}
	
	@GetMapping("/add")
	public String addResearchDocument() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h3>Add a research document</h3>");
		site.append("<p>To add a document you fill in the discription of the research, you fill in the prof ids (use valid ones from the professor service), enter the name of the room (ex. B2.035), enter the estimated price and enter the amount of hours that need to be scheduled for that research.</p>");
		site.append("<form method=\"post\" action=\"/api/research/add\">");
		site.append("Research Description: <input required type=\"textarea\" name=\"description\">");
		site.append("Termination date: <input required type=\"date\" name=\"date\">");
		site.append("Prof id 1: <input required type=\"text\" name=\"profId1\">");
		site.append("Prof id 2: <input type=\"text\" name=\"profId2\">");
		site.append("Prof id 3: <input type=\"text\" name=\"profId3\">");
		site.append("Estimated Price: <input required type=\"number\" name=\"estimatedPrice\">");
		site.append("Needed Hours: <input required type=\"number\" name=\"neededHours\">");
		site.append("Room id: <input required type=\"text\" name=\"lokaalId\">");
		site.append("<button type=\"submit\">Create this Research Document</button>");
		site.append("</form>");
		site.append("</form>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@PostMapping("/add")
	public DeferredResult<ResearchDocument> addResearchDocument(@RequestParam("description") String description, @RequestParam("date") String date, @RequestParam("profId1") String profId1, @RequestParam("profId2") String profId2, @RequestParam("profId3") String profId3, @RequestParam("estimatedPrice") double estimatedPrice, @RequestParam("neededHours") int hours, @RequestParam("lokaalId") String lokaalId) {
		DeferredResult<ResearchDocument> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		ArrayList<String> profIds = new ArrayList<String>();
		profIds.add(profId1);
		if (!profId2.isEmpty())
			profIds.add(profId2);
		if (!profId3.isEmpty())
			profIds.add(profId3);
		boolean profIdsValid = true;
		for (String profId: profIds) {
			try {
				this.onderzoekService.isValidProfId(profId);
			} catch (OnderzoekServiceException e) {
				profIdsValid = false;	
			}
		}
		if (profIdsValid) {
			ResearchDocument researchDocument = this.onderzoekService.createDocument(description, LocalDate.parse(date), profIds, estimatedPrice, hours, lokaalId);
			result.setResult(researchDocument);
		} else {
			result.setErrorResult("ERROR: could not add vak because one or more profIds " + profIds + " does not exist");
		}
		return result;
	}
	
	@GetMapping("/reserve")
	public String reserveRoom() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h3>Schedule an hour of the research</h3>");
		site.append("<form method=\"post\" action=\"/api/research/reserve\">");
		site.append("Document name: <select required name=\"documentId\">");
		for(ResearchDocument rd : this.repo.getResearchDocumentWithStatusAndReserveStatus(Status.APROVED, ReserveStatus.UNRESERVED)){
			site.append("<option value=\"" + rd.getId() + "\">"+rd.toString()+"</option>");
		}
		site.append("</select>");
		site.append("Date: <input required type=\"date\" name=\"date\">");
		site.append("Time: <select required name=\"time\"><option value=\"H8M15\">8H15</option><option value=\"H9M15\">9H15</option><option value=\"H10M30\">10H30</option><option value=\"H11M15\">11H30</option><option value=\"H12M30\">12H30</option><option value=\"H13M30\">13H30</option><option value=\"H14M30\">14H30</option><option value=\"H15M45\">15H45</option><option value=\"H16M45\">16H45</option><option value=\"H18M00\">18H00</option><option value=\"H19M00\">19H00</option></select>");
		site.append("<button type=\"submit\">Reserve this room</button>");
		site.append("</form>");
		site.append("</form>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@PostMapping("/reserve")
	public DeferredResult<String> postReserveRoom(@RequestParam("documentId") String documentId, @RequestParam("date") String date, @RequestParam("time") Time time) {
		DeferredResult<String> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		Boolean ok = this.onderzoekService.reserveRoom(documentId, LocalDate.parse(date), time);
		if (ok) {
			result.setResult("Scheduling started... check the reservation service to view the result, if the service does not show the result, the prof or room was not available.");
		} else {
			result.setErrorResult("ERROR: could not schedule " + documentId + " " + date + " " + time);
		}
		return result;
	}
	
	@GetMapping("/{id}")
	public ResearchDocument ResearchDocumentById(@PathVariable("id") String id) {
		return repo.findById(id).get();
	}
	
	/* kan niet met de index page
	@GetMapping("")
	public List<ResearchDocument> getResearchDocuments() {
		return repo.getResearchDocuments(); 
	}
	*/
	
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResearchDocument postResearchDocument(@RequestBody ResearchDocument m) {
		ResearchDocument doc = this.onderzoekService.createDocument(m.getDescription(), m.getTerminationDate(), m.getProfIds(), m.getEstimatedPrice(), m.getNeededHours(), m.getLokaalId());
		repo.save(doc);
		return doc;
	}
	
	
	@PutMapping(consumes = "application/json")
	public ResearchDocument putResearchDocument(@RequestBody ResearchDocument m) {
		repo.deleteById(m.getId());
		repo.save(m);
		return m;
	}
	
	@DeleteMapping("/{id}")
	public void deleteResearchDocumentById(@PathVariable("id") String id) {
		repo.deleteById(id);
	}
	
	@DeleteMapping("/")
	public void deleteAll() {
		repo.deleteAll();
	}
	
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}
}
