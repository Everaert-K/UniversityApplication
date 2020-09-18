package com.systeemontwerp.studentservice.adapters.rest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.systeemontwerp.studentservice.domain.Address;
import com.systeemontwerp.studentservice.domain.PaymentCompleteListener;
import com.systeemontwerp.studentservice.domain.RegistrationStatus;
import com.systeemontwerp.studentservice.domain.Student;
import com.systeemontwerp.studentservice.domain.StudentService;
import com.systeemontwerp.studentservice.domain.StudentServiceException;
import com.systeemontwerp.studentservice.persistence.StudentRepository;

@RestController
@RequestMapping(path = "/api/students")
@CrossOrigin(origins = "*")
public class StudentRestController implements PaymentCompleteListener {

	private final Logger logger = Logger.getLogger(StudentRestController.class);
	private HashMap<String, DeferredResult<Student>> resultMap;

	private StudentRepository studentRepository;
	private StudentService studentService;

	@Autowired
	public StudentRestController(StudentRepository studentRepository, StudentService studentService) {
		this.studentRepository = studentRepository;
		this.studentService = studentService;
		this.resultMap = new HashMap<>();
	}
	
	@PostConstruct
	private void registerListener() {
		this.studentService.registerListener(this);
	}
	
	@GetMapping("")
	public String showHomePage() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the Student Service!</h1>");
		site.append("<p>The student service is responsible for the management of the students.</p>");
		site.append("<p>It keeps track of different kinds of data for example student number, address, ...</p>");
		site.append("<p>By clicking on the link 'Register a student', you can fill in a form to register a student.</p>");
		site.append("<p>If the registration was successful, the status of the student object will be 'SUCCESS'. Otherwise, it will be 'FAILED'.</p>");
		site.append("<h2>View information</h2>");
		site.append("<a href=\"/api/students/list\">Show me the students</a><br>");
		site.append("<p>To view specific student -> get /api/students/{student-id}</p>");
		site.append("<a href=\"/api/students/addStudent\">Register a student<br>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}

	@GetMapping("/list")
	public List<Student> getAllStudents() {
		return (List<Student>) studentRepository.findAll();
	}
	
	@GetMapping("/addStudent")
	public String addStudentWebpage() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h4>Personal information</h4>");
		site.append("<form method=\"post\" action=\"/api/students/add\">");
		site.append("First name: <input required type=\"text\" name=\"firstName\">");
		site.append("Last name: <input required type=\"text\" name=\"lastName\">");
		site.append("Birthdate: <input required type=\"date\" name=\"birthDate\">");
		site.append("Email: <input required type=\"text\" name=\"email\">");
		site.append("<h4>Address</h4>");
		site.append("Street: <input required type=\"text\" name=\"street\">");
		site.append("House number: <input required type=\"text\" name=\"houseNumber\">");
		site.append("Appartment number (if necessary): <input required type=\"text\" name=\"appartmentNumber\">");
		site.append("ZIP code: <input required type=\"text\" name=\"zipCode\">");
		site.append("City: <input required type=\"text\" name=\"city\">");
		site.append("State: <input required type=\"text\" name=\"state\">");
		site.append("Country: <input required type=\"text\" name=\"country\">");
		site.append("<br>");
		site.append("<button type=\"submit\">Add this student</button>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}

	@GetMapping("/count")
	public Long getNumberOfStudents() {
		return studentRepository.count();
	}

	@GetMapping("/{id}")
	public Student getSpecificStudent(@PathVariable("id") String id) {
		return studentRepository.findByStudentNumber(id);
	}
	
	@PostMapping("/add")
	public DeferredResult<Student> registerStudentViaForm(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("birthDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate, @RequestParam("email") String email, @RequestParam("street") String street,
			@RequestParam("houseNumber") String houseNumber, @RequestParam("appartmentNumber") String appartmentNumber, @RequestParam("zipCode") String zipCode,
			@RequestParam("city") String city, @RequestParam("state") String state, @RequestParam("country") String country) {
		
		Student student = new Student(firstName, lastName, new Address(street, houseNumber, appartmentNumber, zipCode, city, state, country), birthDate, email);
		
		this.logger
				.info("Received request to register student: " + student.getFirstName() + " " + student.getLastName());
		DeferredResult<Student> result = new DeferredResult<>(10000l);

		student.setStatus(RegistrationStatus.PENDING);
		Student savedStudent = studentRepository.save(student);

		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
			this.studentService.paymentFailed(savedStudent.getStudentNumber());
		});

		this.resultMap.put(savedStudent.getStudentNumber(), result);

		try {
			this.studentService.registerStudent(savedStudent.getStudentNumber());
		} catch (StudentServiceException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
			this.resultMap.remove(savedStudent.getStudentNumber());
		}

		return result;
	}

	@PostMapping(consumes = "application/json")
	public DeferredResult<Student> registerStudent(@RequestBody Student student) {
		this.logger
				.info("Received request to register student: " + student.getFirstName() + " " + student.getLastName());
		DeferredResult<Student> result = new DeferredResult<>(10000l);

		student.setStatus(RegistrationStatus.PENDING);
		Student savedStudent = studentRepository.save(student);

		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
			this.studentService.paymentFailed(savedStudent.getStudentNumber());
		});

		this.resultMap.put(savedStudent.getStudentNumber(), result);

		try {
			this.studentService.registerStudent(savedStudent.getStudentNumber());
		} catch (StudentServiceException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
			this.resultMap.remove(savedStudent.getStudentNumber());
		}

		return result;
	}

	@DeleteMapping("/{id}")
	public void deleteStudent(@PathVariable("id") String id) {
		studentRepository.deleteById(id);
	}

	@Override
	public void onPaymentComplete(Student student) {
		if (this.resultMap.containsKey(student.getStudentNumber())) {
			DeferredResult<Student> result = this.resultMap.get(student.getStudentNumber());
			this.resultMap.remove(student.getStudentNumber());
			result.setResult(student);
		}
	}
	
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}

}
