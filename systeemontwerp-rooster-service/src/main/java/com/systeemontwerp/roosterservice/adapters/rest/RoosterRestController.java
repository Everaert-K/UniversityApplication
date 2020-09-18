package com.systeemontwerp.roosterservice.adapters.rest;

import java.time.LocalDate;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.systeemontwerp.roosterservice.domain.Rooster;
import com.systeemontwerp.roosterservice.domain.RoosterService;
import com.systeemontwerp.roosterservice.domain.RoosterServiceException;
import com.systeemontwerp.roosterservice.domain.Time;
import com.systeemontwerp.roosterservice.domain.Vak;
import com.systeemontwerp.roosterservice.domain.VakStatus;
import com.systeemontwerp.roosterservice.persistence.RoosterRepository;
import com.systeemontwerp.roosterservice.persistence.VakRepository;

@RestController
@RequestMapping("/api/rooster")
public class RoosterRestController {

	private final RoosterRepository roosterRepository;
	private final VakRepository vakRepository;
	private final RoosterService roosterService;
	private final Logger logger = Logger.getLogger(RoosterRestController.class);
	
	@Autowired
	private RoosterRestController(RoosterRepository roosterRepository, VakRepository vakRepository, RoosterService roosterService) {
		this.roosterRepository = roosterRepository;
		this.vakRepository = vakRepository;
		this.roosterService = roosterService;
	}
	
	//The general html page
	@GetMapping("")
	public String showIndex() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the RoosterPage!</h1>");
		site.append("<p>In this service you can add vakken. A vak is added to the database after the id of the prof connected to the vak is verified trough the Professor service.</p>");
		site.append("<p>After creating a a vak you can schedule a hour. This checks if the professor (throught the Professor service) and the room (through the Reservering service) are available at the given hour and time.</p>");
		site.append("<p>If the professor and the room are available the vak is scheduled at the given time and date, the room is reserved in the Reservering Service and the Reservering service will book the professor his hours.</p>");
		site.append("<h2>View information Vakken</h2>");
		site.append("<a href=\"/api/rooster/vak/show\">Show me the vakken</a><br>");
		site.append("<p>To view specific vak -> get /api/rooster/vak/{vak-id}</p>");
		site.append("<a href=\"/api/rooster/vak/add\">Add a vak</a><br>");
		site.append("<h2>View information Rooster</h2>");
		site.append("<a href=\"/api/rooster/show\">Show me the rooster</a><br>");
		site.append("<p>To view specific rooster -> get /api/rooster/{rooster-id}</p>");
		site.append("<a href=\"/api/rooster/add\">Schedule a vak<br>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/vak/show")
	public List<Vak> showVakken(){
		/* DEPRICATED SITE
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<ul>");
		for(Vak vak : this.vakRepository.findAll()){
			site.append("<li>"+vak.toString()+"</li>");
			}
		site.append("</ul>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
		*/
		return this.vakRepository.findAll();
	}
	
	@GetMapping("/vak/add")
	public String addVak() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h3>Add a vak</h3>");
		site.append("<p>To add a vak you fill in the name of the vak, you fill in your prof id (use a valid one from the professor service), enter the name of the room (ex. B2.035) and enter the amount of hours that need to be scheduled for that vak.</p>");
		site.append("<form method=\"post\" action=\"/api/rooster/vak/add\">");
		site.append("Vak name: <input required type=\"text\" name=\"name\">");
		site.append("Prof id: <input required type=\"text\" name=\"profId\">");
		site.append("Room id: <input required type=\"text\" name=\"lokaalId\">");
		site.append("Hours: <input required type=\"number\" name=\"hours\">");
		site.append("<button type=\"submit\">Add vak</button>");
		site.append("</form>");
		site.append("</form>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@PostMapping("/vak/add")
	public DeferredResult<Vak> addVak(@RequestParam("name") String name, @RequestParam("profId") String profId, @RequestParam("lokaalId") String lokaalId, @RequestParam("hours") int hours) {
		this.logger.info("Received reservation from " + profId + " for room: " + lokaalId);
		DeferredResult<Vak> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		try {
			if (this.roosterService.isValidProfId(profId)) {
				try {
					Vak vak = new Vak(name, profId, lokaalId, hours);
					this.vakRepository.save(vak);
					result.setResult(vak);
				} catch (Exception e) {
					result.setErrorResult("ERROR: could not add vak because there already is a vak with this name in the db");
				}
			}
		} catch (RoosterServiceException e) {
			result.setErrorResult("ERROR: could not add vak because the profid " + profId + " does not exist");
		}
		return result;
	}
	
	@GetMapping("/show")
	public List<Rooster> showRooster(){
		/* DEPRICATED SITE
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<ul>");
		for(Rooster rooster : this.roosterRepository.findAll()){
			site.append("<li>"+rooster.toString()+"</li>");
		}
		site.append("</ul>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
		*/
		return this.roosterRepository.findAll();
	}
	
	@GetMapping("/add")
	public String addRooster() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h3>Schedule an hour of a vak</h3>");
		site.append("<p>To reserve a room for a vak you choose a vak from the combobox and select the date and time.</p>");
		site.append("<p>You schould check the reservering service to see if the room for the vak is not already reserved at that time and date and.</p>");
		site.append("<p>you schould check the professor service to see if the prof has a free hour for that time and date.</p>");
		site.append("<form method=\"post\" action=\"/api/rooster/add\">");
		site.append("Vak name: <select required name=\"vakId\">");
		for(Vak vak : this.vakRepository.findByStatus(VakStatus.UNCHEDULED)){
			site.append("<option value=\"" + vak.getId() + "\">"+vak.toString()+"</option>");
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
	
	@PostMapping("/add")
	public DeferredResult<String> postRooster(@RequestParam("vakId") String vakId, @RequestParam("date") String date, @RequestParam("time") Time time) {
		this.logger.info("Received schedule for " + vakId + " for " + date + " and time " + time);
		DeferredResult<String> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		Boolean ok = this.roosterService.scheduleClass(vakId, LocalDate.parse(date), time);
		if (ok) {
			result.setResult("Scheduling started... check the rooster to view the result, if the rooster does not show the result, the room was not available.");
		} else {
			result.setErrorResult("ERROR: could not schedule " + vakId + " " + date + " " + time + "because the prof is not available");
		}
		return result;
	}
	
	@GetMapping("/vak/{id}")
	public Vak getVakById(@PathVariable("id") String id) {
		return this.vakRepository.findById(id).orElse(null);
	}
	
	@GetMapping("/{id}")
	public Rooster getRoosterById(@PathVariable("id") String id) {
		return this.roosterRepository.findById(id).orElse(null);
	}
	
	@DeleteMapping("/vak/{id}")
	public void deleteVakById(@PathVariable("id") String id) {
		this.vakRepository.deleteById(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteRoosterById(@PathVariable("id") String id) {
		this.roosterRepository.deleteById(id);
	}
	
	@DeleteMapping("/vak/")
	public void deleteAllVakken() {
		this.vakRepository.deleteAll();
	}
	
	@DeleteMapping("/")
	public void deleteAllRooster() {
		this.roosterRepository.deleteAll();
	}
	
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}
	
}
