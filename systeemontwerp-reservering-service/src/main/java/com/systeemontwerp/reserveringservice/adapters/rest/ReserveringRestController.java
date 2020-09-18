package com.systeemontwerp.reserveringservice.adapters.rest;

import java.time.LocalDate;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.systeemontwerp.reserveringservice.domain.Reservering;
import com.systeemontwerp.reserveringservice.domain.ReserveringService;
import com.systeemontwerp.reserveringservice.domain.ReserveringServiceException;
import com.systeemontwerp.reserveringservice.domain.ReserveringType;
import com.systeemontwerp.reserveringservice.domain.Time;
import com.systeemontwerp.reserveringservice.persistence.ReserveringRepository;

@RestController
@RequestMapping("/api/reservering")
public class ReserveringRestController {

	private final ReserveringRepository reserveringRepository;
	private final ReserveringService reserveringService;
	private final Logger logger = Logger.getLogger(ReserveringRestController.class);
	
	@Autowired
	private ReserveringRestController(ReserveringRepository reserveringRepository, ReserveringService reserveringService) {
		this.reserveringRepository = reserveringRepository;
		this.reserveringService = reserveringService;
	}
	
	//The general html page
	@GetMapping("")
	public String showIndex() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the ReserveringPage!</h1>");
		site.append("<p>This service is used by the Onderzoek service and the Rooster service to reserve a room for a reseach and a class.</p>");
		site.append("<p>A student can use this service to reserve a room for an activity. A room can only be reserved if the student gives a valid student id. The student id is checked through the Student service</p>");
		site.append("<h2>View information</h2>");
		site.append("<a href=\"/api/reservering/show\">Show me the reservations</a><br>");
		site.append("<p>To view specific reservation -> get /api/reservering/{reservering-id}</p>");
		site.append("<a href=\"/api/reservering/reserve\">Reserve a room for a activity<br>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/show")
	public List<Reservering> showReservations(){
		/* DEPRICATED SITE
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<ul>");
		for(Reservering rs : this.reserveringRepository.findAll()){
			site.append("<li>"+rs.toString()+"</li>");
			}
		site.append("</ul>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
		*/
		return this.reserveringRepository.findAll();
	}
	
	@GetMapping("/reserve")
	public String reserveRoom() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h3>Reserve a room</h3>");
		site.append("<p>To reserve a room for an activity you fill in your student id (use a valid one from the student service), enter the name of the room (ex. B2.035) and select a date and hour.</p>");
		site.append("<form method=\"post\" action=\"/api/reservering/reserve\">");
		site.append("Student id: <input required type=\"text\" name=\"profId\">");
		site.append("Room id: <input required type=\"text\" name=\"lokaalId\">");
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
	public DeferredResult<Reservering> addReservering(@RequestParam("profId") String profId, @RequestParam("lokaalId") String lokaalId, @RequestParam("date") String date, @RequestParam("time") Time time) {
		this.logger.info("Received reservation from " + profId + " for room: " + lokaalId);
		DeferredResult<Reservering> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		try {
			this.reserveringService.isValidStudentId(profId);
		} catch (ReserveringServiceException e) {
			result.setErrorResult("ERROR: cannot find student id " + profId);
		}
		Reservering reservering = this.reserveringService.reserveRoom(lokaalId, profId, LocalDate.parse(date), time, ReserveringType.ACTIVITY);
		if (reservering != null) {
			result.setResult(reservering);
		} else {
			result.setErrorResult("ERROR: room is already booked on " + date + " " + time);
		}
		return result;
	}
	
	@GetMapping("/reservations/{id}")
	public Reservering getReserveringById(@PathVariable("id") String id) {
		return this.reserveringRepository.findById(id).orElse(null);
	}
	
	@DeleteMapping("/{id}")
	public void deleteReserveingById(@PathVariable("id") String id) {
		this.reserveringRepository.deleteById(id);
	}
	
	@DeleteMapping("/")
	public void deleteAll() {
		this.reserveringRepository.deleteAll();
	}
	
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}
	
}
