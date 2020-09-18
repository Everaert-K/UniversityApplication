package com.systeemontwerp.professorservice.adapters.rest;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.systeemontwerp.professorservice.domain.Hours;
import com.systeemontwerp.professorservice.domain.Professor;
import com.systeemontwerp.professorservice.domain.Time;
import com.systeemontwerp.professorservice.persistence.HoursRepository;
import com.systeemontwerp.professorservice.persistence.ProfessorRepository;

@RestController
@RequestMapping(path = "/api/professors")
@CrossOrigin(origins = "*")
public class ProfessorRestController {
	private ProfessorRepository professorRepository;
	private HoursRepository hoursRepository;
	
	@Autowired
	public ProfessorRestController(ProfessorRepository professorRepository, 
			HoursRepository hoursRepository) {
		this.professorRepository = professorRepository;
		this.hoursRepository = hoursRepository;
	}
	
	@GetMapping("/list")
	public List<Professor> getAllStudents() {
		return (List<Professor>) professorRepository.findAll();
	}
	
	@GetMapping("")
	public String showHomePage() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the Professor Service!</h1>");
		site.append("<p>This service is responsible for the management of the professors.</p>");
		site.append("<p>Each professor keeps track of his available hours. These moments can then be reserved for lessons or researches.</p>");
		site.append("<p>By surfing to the link below ('Reserve an available hour'), you can reserve an available moment of a professor.</p>");
		site.append("<p>You need to fill in the employee number of a professor, a date and the hour that you want to reserve.</p>");
		site.append("<p>The available moments are namely saved per hour so for example 8:15 to 9:15.</p>");
		site.append("<p>An appropiate message will then be shown (either the professor was free at that moment or not).</p>");
		site.append("<h2>View information</h2>");
		site.append("<a href=\"/api/professors/list\">Show me the professors</a><br>");
		site.append("<p>To view specific professor -> get /api/professors/{employee-number}</p>");
		site.append("<p>To view the available hours of a professor -> get /api/professors/hours/{employee-number}</p>");
		site.append("<a href=\"/api/professors/reserveHours\">Reserve an available hour<br>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/reserveHours")
	public String reserveHours() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");

		site.append("<h3>Reserve an available hour</h3>");
		site.append("<form method=\"post\" action=\"/api/professors/reserve\">");
		site.append("Employee number of professor: <input required type=\"text\" name=\"profId\">");
		site.append("Date (give a value between 03/02/2019 and 07/02/2019): <input required type=\"date\" name=\"date\">");
		site.append("Time: <select required name=\"time\"><option value=\"H8M15\">8H15</option><option value=\"H9M15\">9H15</option><option value=\"H10M30\">10H30</option><option value=\"H11M15\">11H30</option><option value=\"H12M30\">12H30</option><option value=\"H13M30\">13H30</option><option value=\"H14M30\">14H30</option><option value=\"H15M45\">15H45</option><option value=\"H16M45\">16H45</option><option value=\"H18M00\">18H00</option><option value=\"H19M00\">19H00</option></select>");
		site.append("<button type=\"submit\">Reserve this hour</button>");
		site.append("</form>");
		
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/{id}")
	public Professor getSpecificProfessor(@PathVariable("id")String id) {
		return professorRepository.findByEmployeeNumber(id);
	}
	
	@GetMapping("/hours/{id}")
	public List<Hours> getAvailableHours(@PathVariable("id")String id) {
		return hoursRepository.findByProfessorEmployeeNumberOrderByDayAscStartAsc(id);
	}
	
	@GetMapping("/hours/{id}/{time}/{date}")
	public Hours getAvailableHoursByStartAndDay(@PathVariable("id")String employeeNumber, @PathVariable("time") Time time, @PathVariable("date")String date) {
		LocalDate ldate = LocalDate.parse(date);
		return this.hoursRepository.findByProfessorEmployeeNumberAndStartAndDay(employeeNumber, time, ldate);
	}
	
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Professor addProfessor(@RequestBody Professor professor) {
		return professorRepository.save(professor);
	}
	
	@PostMapping(path = "/hours", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Hours addAvailableHour(@RequestBody Hours hours) {
		return hoursRepository.save(hours);
	}
	
	@PostMapping("/reserve")
	public String reserveHoursViaForm(@RequestParam("profId") String profId, 
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("time") Time time) {
		Hours hours = this.hoursRepository.findByProfessorEmployeeNumberAndStartAndDay(profId, time, date);
		if(hours != null) {
			hoursRepository.deleteById(hours.getId());
			StringBuilder site = new StringBuilder();
			site.append("<html>");
			site.append("<head></head>");
			site.append("<body>");
			site.append("This moment was reserved successfully");
			site.append("</body>");
			site.append("</html>");
			return site.toString();
		}
		
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("The professor is not available at this moment");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@DeleteMapping("/reserve/{id}")
	public void reserveHours(@PathVariable("id")String id) {
		hoursRepository.deleteById(id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteProfessor(@PathVariable("id")String id) {
		professorRepository.deleteById(id);
	}
	
}
