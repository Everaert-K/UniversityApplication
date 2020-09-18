package com.systeemontwerp.cursusservice.adapteres.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.systeemontwerp.cursusservice.adapteres.messaging.PaymentMethod;
import com.systeemontwerp.cursusservice.domain.BuyItemCompleteListener;
import com.systeemontwerp.cursusservice.domain.CursusService;
import com.systeemontwerp.cursusservice.domain.CursusServiceException;
import com.systeemontwerp.cursusservice.domain.LiteratureItem;
import com.systeemontwerp.cursusservice.domain.LiteratureItemReservation;
import com.systeemontwerp.cursusservice.persistence.LiteratureItemRepository;
import com.systeemontwerp.cursusservice.persistence.ReservationRepository;

@RestController
@RequestMapping("/api/courses")
public class CursusRestController implements BuyItemCompleteListener {
	
private final LiteratureItemRepository literatureItemRepository;
private final ReservationRepository reservationRepository;
private final CursusService cursusService;

private final Logger logger = Logger.getLogger(CursusRestController.class);

private HashMap<String, DeferredResult<LiteratureItemReservation>> resultMap;
	
	@Autowired
	private CursusRestController(CursusService cursusService, LiteratureItemRepository literatureItemRepository, ReservationRepository reservationRepository) {
		this.cursusService = cursusService;
		this.literatureItemRepository = literatureItemRepository;
		this.reservationRepository = reservationRepository;
		this.resultMap = new HashMap<>();
	}
	
	@PostConstruct
	private void registerListener() {
		this.cursusService.registerListener(this);
	}
	
	//The general html page
	@GetMapping("")
	public String showIndex() {
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Welcome to the CursusPage!</h1>");
		site.append("<p>This service manages all the books and cursusses. Students can buy items from the library or request a new book."
				+ " Stock can be refilled by the library manager and new cursusses can be uploaded by profs. If a book or cursus"
				+ " is not in stock, the student will be added to its reservation list.</p>");
		site.append("<h2>View information</h2>");
		site.append("<a href=\"/api/courses/library\">Show me the library</a><br>");
		site.append("<a href=\"/api/courses/reservations\">Show me the reservations</a><br><br>");
		site.append("<p>To view specific item and its possibilities (restock/buy/delete) -> get /api/courses/{item-id}</p>");
		site.append("<p>To view specific reservation -> /api/courses/reservations/{item-id}</p>");
		site.append("<h2>Operations for professors</h2>");
		site.append("<h3>Upload a new cursus</h3>");
		site.append("<p>If the upload succeeds, your cursus will be visible in the library section. Students can also buy your "
				+ "cursus now.</p>");
		site.append("<form method=\"post\" action=\"/api/courses/upload_cursus\">");
		site.append("Your id: <input type=\"text\" name=\"profid\">");
		site.append("The title: <input type=\"text\" name=\"title\">");
		site.append("Your full name: <input type=\"text\" name=\"author\">");
		site.append("The content: <input type=\"textfield\" name=\"content\">");
		site.append("The price: <input type=\"number\" step=\"0.01\" min=\"0\" name=\"price\">");
		site.append("<button type=\"submit\">Upload cursus!</button>");
		site.append("</form>");
		site.append("<h2>Operations for professors and students</h2>");
		site.append("<h3>Request a new book in the library</h3>");
		site.append("<p>Use this to request a book from the bookstore in the library. Note that by default there will be no stock for "
				+ "this book. To restock this item, browse for this item and restock it.</p>");
		site.append("<form method=\"post\" action=\"/api/courses/request_book\">");
		site.append("The title: <input type=\"text\" name=\"title\">");
		site.append("The author: <input type=\"text\" name=\"author\">");
		site.append("<button type=\"submit\">Request this book!</button>");
		site.append("</form>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@GetMapping("/{id}")
	public String showItem(@PathVariable("id") String literatureid) {
		LiteratureItem item = this.literatureItemRepository.findById(literatureid).orElse(new LiteratureItem());
		if(item.getId() == null)
			return "Not a literatureitem id";
		
		//----------HTML page for a specific literature item--------------
		StringBuilder site = new StringBuilder();
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		site.append("<h1>Item: "+ literatureid +"</h1>");
		site.append("<h2>Information</h2>");
		site.append("<p>"+ item.toString() +"</p>");
		site.append("<h3>Buy this item</h3>");
		site.append("<p>When selecting EPURSE the amount is taken from your card. If you select"
				+ "Bancontact or PayPal however, make sure you have internet so these services can be used."
				+ " To view your transaction, please visit the <a href=\"http://localhost:8080/api/financial\">Financial service</a>."
				+ " If a cursus is bought, the professor will also receive his/her payment share. If enough books are in stock, you"
				+ " can retrieve your book/cursus. Otherwise you will be added to the reserve list. View the library for the reservees "
				+ "for each book or the reservations too see if your item was reserved or completed.</p>");
		site.append("<form method=\"post\" action=\"" + literatureid + "/buy\">");
		site.append("Your id: <input type=\"text\" name=\"buyerId\">");
		site.append("Paymentmethod: <select name=\"method\"><option value=\"EPURSE\">Epurse</option><option value=\"BANCONTACT\">Bancontact</option><option value=\"PAYPAL\">PayPal</option></select>");
		site.append("<button type=\"submit\">Buy this item!</button>");
		site.append("</form>");
		site.append("<h3>Restock this item</h3>");
		site.append("<p>If this item is a book, the total price will be transferred to the bookstore. If this item"
				+ " is a course no revenue will emediately be paid to the prof but each time a student buys his/her"
				+ " course, 50% of the amount will be transferred to the prof. To view these transactions please"
				+ " visit the <a href=\"http://localhost:8080/api/financial\">Financial service</a></p>");
		site.append("<form method=\"post\" action=\"" + literatureid + "/restock\">");
		site.append("Amount: <input type=\"number\" name=\"amount\">");
		site.append("<button type=\"submit\">Restock this item!</button>");
		site.append("</form>");
		site.append("<h3>Delete this item</h3>");
		site.append("<form method=\"post\" action=\"" + literatureid + "/delete\">");
		site.append("<button type=\"submit\">Delete this item!</button>");
		site.append("</form>");
		site.append("</body>");
		site.append("</html>");
		return site.toString();
	}
	
	@PostMapping("/{id}/delete")
	public String deleteItem(@PathVariable("id") String literatureId) {
		LiteratureItem item = this.literatureItemRepository.findById(literatureId).orElse(new LiteratureItem());
		if(item.getId() == null)
			return "Not a literatureitem id";
		this.literatureItemRepository.deleteById(literatureId);
		return "Ok";
	}
	
	@GetMapping("/library")
	public List<LiteratureItem> getAllItems(){
		return this.literatureItemRepository.findAll();
	}
	
	@GetMapping("/reservations")
	public List<LiteratureItemReservation> getAllReservations(){
		return this.reservationRepository.findAll();
	}
	
	@PostMapping("/request_book")
	public DeferredResult<LiteratureItem> requestBook(@RequestParam("title") String title, @RequestParam("author") String author) {
		DeferredResult<LiteratureItem> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		
		try {
			result.setResult(this.cursusService.requestBookInLibrary(title, author));
		} catch(CursusServiceException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
		}
		
		return result;
	}
	
	@PostMapping("/upload_cursus")
	public DeferredResult<LiteratureItem> uploadCursus(@RequestParam("title") String title, 
			@RequestParam("author") String author, @RequestParam("profid") String id, @RequestParam("price") double price, 
			@RequestParam("content") String content){
		DeferredResult<LiteratureItem> result = new DeferredResult<>(100000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		
		try {
			result.setResult(this.cursusService.uploadCursus(title, author, id, price, content.getBytes()));
		}
		catch(CursusServiceException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
		}
		
		return result;
	}
	
	@GetMapping("/reservations/{id}")
	public LiteratureItemReservation getCursusById(@PathVariable("id") String id) {
		return this.reservationRepository.findById(id).orElse(new LiteratureItemReservation());
	}
	
	@PostMapping("/{id}/restock")
	public DeferredResult<LiteratureItem> restockItem(@PathVariable("id") String literatureId, @RequestParam("amount") int amount){
		DeferredResult<LiteratureItem> result = new DeferredResult<>(100000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});

		try {
			result.setResult(this.cursusService.restockItem(literatureId, amount));
		} catch(CursusServiceException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
		}
		
		return result;
	}
	
	@PostMapping("/{id}/buy")
	public DeferredResult<LiteratureItemReservation> buyItem(@PathVariable("id") String literatureId, @RequestParam("buyerId") String buyerId, @RequestParam("method") PaymentMethod method){
		this.logger.info("Received order for book/cursus: " + literatureId);
		DeferredResult<LiteratureItemReservation> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
			this.cursusService.setFailedPayments(literatureId, buyerId);
		});

		this.resultMap.put(buyerId, result);
		
		try {
			this.cursusService.buyItem(literatureId, buyerId, method);
		} catch(CursusServiceException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
			this.resultMap.remove(buyerId);
		}
		
		return result;
	}
	
	//The simulates the external bookstore, for now just give fake prices
	@GetMapping("/fakebookstore/price")
	public double getPrice(@RequestParam("title") String title, @RequestParam("author") String author){
		double random = 5.0 + Math.random() * 15.0;
		double temp = Math.floor(random * 100);
		return temp / 100.0;
	}
	
	//Fallback to catch extra exceptions thrown by the application
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}
	
	@Override
	public void onBuyItemComplete(LiteratureItemReservation reservation) {
		if(this.resultMap.containsKey(reservation.getReserveeId())) {
			DeferredResult<LiteratureItemReservation> result = this.resultMap.get(reservation.getReserveeId());
			this.resultMap.remove(reservation.getReserveeId());
			result.setResult(reservation);
		}
	}
	
	
}
