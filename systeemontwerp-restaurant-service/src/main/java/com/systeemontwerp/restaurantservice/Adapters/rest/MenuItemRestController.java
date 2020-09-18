package com.systeemontwerp.restaurantservice.Adapters.rest;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.systeemontwerp.restaurantservice.Domain.Type;
import com.systeemontwerp.restaurantservice.Adapters.messaging.PaymentRequest;
import com.systeemontwerp.restaurantservice.Adapters.messaging.PaymentResponse;
import com.systeemontwerp.restaurantservice.Domain.BuyItemCompleteListener;
import com.systeemontwerp.restaurantservice.Domain.MenuItem;
import com.systeemontwerp.restaurantservice.Domain.OrderFailedException;
import com.systeemontwerp.restaurantservice.Domain.Purchase;
import com.systeemontwerp.restaurantservice.Domain.RestaurantService;
import com.systeemontwerp.restaurantservice.Persistence.MenuItemRepository;

@RestController
@RequestMapping(path="/api/restaurant/menuitems")
@CrossOrigin(origins="*")
public class MenuItemRestController implements BuyItemCompleteListener {
	
	private MenuItemRepository repo;
	private RestaurantService restaurantService;
	private HashMap<String,DeferredResult<Purchase>> resultmap;
	
	@Autowired
	public MenuItemRestController(MenuItemRepository repo, RestaurantService restaurantService) {
		this.repo = repo;
		this.restaurantService = restaurantService;
		this.resultmap = new HashMap<String,DeferredResult<Purchase>>();
	}
	
	@GetMapping("/{id}")
	public MenuItem menuItemById(@PathVariable("id") String id) {
		return repo.findById(id).get();
	}
	
	@GetMapping("")
	public List<MenuItem> getMenuItems() { 
		return repo.giveItemsWithQuantityBiggerOrEqual(1); // items that are not in store do not get showed
	}
	
	
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public MenuItem postMenuItem(@RequestBody MenuItem m) {
		repo.save(m);
		return m;
	}
	
	@PutMapping(consumes = "application/json")
	public MenuItem putMenuItem(@RequestBody MenuItem m) {
		repo.deleteById(m.getName());
		repo.save(m);
		return m;
	}
	
	@DeleteMapping("/{id}")
	public void deleteMenuItemById(@PathVariable("id") String id) {
		repo.deleteById(id);
	}
	
	@DeleteMapping("/")
	public void deleteAll() {
		repo.deleteAll();
	}
	
	/////////////////////////////////// websites /////////////////////////////////////////////////////////////
	
	@GetMapping("/menu")
	public String geefMenu() {
		
		String zin = "<HTML><HEAD><TITLE>Menu Van De Dag:</TITLE></HEAD>";
		zin+="<BODY><h1>MENU</h1>";
		
		zin+="<h2>ZETMEEL</h2>";
		ArrayList<MenuItem> l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.ZETMEEL);
		for(int i=0;i<l.size();i++) {
			zin+="<p>";
			zin+=l.get(i).getName();
			zin+="</p>";
		}
		
		zin+="<h2>BROODJES</h2>";
		l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.BROODJE);
		for(int i=0;i<l.size();i++) {
			zin+="<p>";
			zin+=l.get(i).getName();
			zin+="</p>";
		}
		
		zin+="<h2>PROTEIN</h2>";
		l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.PROTEIN);
		for(int i=0;i<l.size();i++) {
			zin+="<p>";
			zin+=l.get(i).getName();
			zin+="</p>";
		}
		
		zin+="<h2>SAUS</h2>";
		l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.SAUS);
		for(int i=0;i<l.size();i++) {
			zin+="<p>";
			zin+=l.get(i).getName();
			zin+="</p>";
		}

		zin+="<a href=\"/api/restaurant/menuitems/bestel\">New order</a>";
		
		zin+="</BODY></HTML>";
		
		return zin;
	}
	
	@GetMapping("/bestel")
	public String giveMenu() {
		String zin = "<HTML><HEAD><TITLE>Menu Van De Dag:</TITLE></HEAD>";
		zin+="<BODY><h1>Bestel</h1>";
		
		zin+="<form method=\"post\" action='/api/restaurant/menuitems/handelBestellingAf'>";
		zin+="<p>ZETMEEL</p>";
		zin+="<select required name='ZETMEEL'>";
		ArrayList<MenuItem> l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.ZETMEEL);
		for(int i=0;i<l.size();i++) {
			zin+="<option value=\"";
			zin+=l.get(i).getName();
			zin+="\">";
			zin+=l.get(i).getName();
			zin+="</option>";
		}
		zin+="</select>";
		
		zin+="<br><br>";
		
		zin+="<p>SAUS</p>";
		zin+="<select required name='SAUS'>";
		l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.SAUS);
		for(int i=0;i<l.size();i++) {
			zin+="<option value=\"";
			zin+=l.get(i).getName();
			zin+="\">";
			zin+=l.get(i).getName();
			zin+="</option>";
		}
		zin+="</select>";
		
		zin+="<br><br>";
		
		zin+="<p>PROTEIN</p>";
		zin+="<select required name='PROTEIN'>";
		l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.PROTEIN);
		for(int i=0;i<l.size();i++) {
			zin+="<option value=\"";
			zin+=l.get(i).getName();
			zin+="\">";
			zin+=l.get(i).getName();
			zin+="</option>";
		}
		zin+="</select>";
		
		zin+="<br><br>";
		
		zin+="<p>BROODJE</p>";
		zin+="<select required name='BROODJE'>";
		l = (ArrayList<MenuItem>) repo.giveItemsWithType(Type.BROODJE);
		for(int i=0;i<l.size();i++) {
			zin+="<option value=\"";
			zin+=l.get(i).getName();
			zin+="\">";
			zin+=l.get(i).getName();
			zin+="</option>";
		}
		zin+="</select>";
		
		zin+="<br><br>";
		
		zin+="<p>BETALEN</p>";
		zin+="nummer (student of prof) indien niet vul -1 in: <input required type=\"text\" name=\"id\">";
		zin+="from: <input required type=\"text\" name=\"from\">";
		zin+="<select required name='METHOD'>";
		zin+="<option value=\"BANCONTACT\">Bancontact</option>";
		zin+="<option value=\"PAYPAL\">PayPal</option>";
		zin+="<option value=\"EPURSE\">Epurse</option>";
		zin+="</select>";
		
		zin+="<br><br>";
		
		zin+="<input type=\"submit\" value=\"Submit\">";
		zin+="</form>"; 
		
		
		zin+="</BODY></HTML>";
		return zin;
	}
	
	@PostMapping("/handelBestellingAf")
	public DeferredResult<Purchase> postBestelling(@RequestParam("ZETMEEL") String zetmeel, @RequestParam("SAUS") String saus, @RequestParam("PROTEIN") String protein, 
			@RequestParam("BROODJE") String broodje, @RequestParam("id")String id, @RequestParam("from") String from, @RequestParam("METHOD") String method) {
		DeferredResult<Purchase> result = new DeferredResult<>(10000l);
		result.onTimeout(() -> {
			result.setErrorResult("ERROR: Timeout");
		});
		
		// order
		HashMap<String,Integer> map = new HashMap<>();
		map.put(zetmeel,1);
		map.put(saus, 1);
		map.put(protein, 1);
		map.put(broodje, 1);
		
		try {
			Purchase p  = this.restaurantService.makePurchase(map, id, from, method);
			/*
			response.onCompletion(() -> {
				result.setResult("Payment succesfull");
			});
			// response.onError(result.setResult("Payment failed"));
			*/
			this.resultmap.put(p.getId(),result);
		} catch (OrderFailedException e) {
			result.setErrorResult("ERROR: " + e.getMessage());
		}
		return result;
	}
	
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}

	@Override
	public void onBuyItemComplete(Purchase purchase) {
		if(this.resultmap.containsKey(purchase.getId())) {
			this.resultmap.get(purchase.getId()).setResult(purchase);
			this.resultmap.remove(purchase.getId());
		}
		
	}
}
