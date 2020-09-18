package com.systeemontwerp.restaurantservice.Adapters.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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

import com.systeemontwerp.restaurantservice.Domain.BuyItemCompleteListener;
import com.systeemontwerp.restaurantservice.Domain.OrderFailedException;
import com.systeemontwerp.restaurantservice.Domain.Purchase;
import com.systeemontwerp.restaurantservice.Domain.RestaurantService;
import com.systeemontwerp.restaurantservice.Persistence.PurchaseRepository;

@RestController
@RequestMapping(path="/api/restaurant/purchases")
@CrossOrigin(origins="*")
public class PurchaseRestController implements BuyItemCompleteListener {
	
	private PurchaseRepository repo;
	private final Map<String, DeferredResult<Purchase>> deferredResults;
	private RestaurantService restaurantservice;
	
	@Autowired
	public PurchaseRestController(PurchaseRepository repo, RestaurantService restaurantservice) {
		this.repo = repo;
		this.restaurantservice = restaurantservice;
		this.deferredResults = new HashMap<>(10);
	}
	@PostConstruct
	private void registerListener() {
		this.restaurantservice.registerListener(this);
	}
	
	@GetMapping("/{id}")
	public Purchase purchaseById(@PathVariable("id") String id) {
		return repo.findById(id).get();
	}
	
	@GetMapping("")
	public List<Purchase> getPurchases() { 
		return (List<Purchase>) repo.findAll(); 
	}
	
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Purchase postPurchase(@RequestBody Purchase m) {
		repo.save(m);
		return m;
	}
	
	@PutMapping(consumes = "application/json")
	public Purchase putPurchase(@RequestBody Purchase m) {
		repo.deleteById(m.getId());
		repo.save(m);
		return m;
	}
	
	@DeleteMapping("/{id}")
	public void deletePurchaseId(@PathVariable("id") String id) {
		repo.deleteById(id);
	}
	
	@DeleteMapping("/")
	public void deleteAll() {
		repo.deleteAll();
	}
	
	/*
	@GetMapping("/request_purchase")
	public DeferredResult<Purchase> handlePurchase(@RequestParam("purchaseId") String purchaseId) {
		DeferredResult<Purchase> deferredResult = new DeferredResult<>(10000l);

		deferredResult.onTimeout(() -> {
			deferredResult.setErrorResult("Request time-out occurred");
		});

		this.deferredResults.put(purchaseId, deferredResult);

		try {
			this.restaurantservice.aankoop(purchaseId);
		} catch (OrderFailedException e) {
			deferredResult.setErrorResult("Failed to purchase: " + e.getMessage());
			this.deferredResults.remove(purchaseId);
		}

		return deferredResult;
	}
	*/
	
	@ExceptionHandler(Exception.class)
	public String noParamException(Exception ex) {
		return "ERROR: " + ex.getMessage();
	}
	@Override
	public void onBuyItemComplete(Purchase purchase) {
		if(this.deferredResults.containsKey(purchase.getId())) {
			DeferredResult<Purchase> result = this.deferredResults.get(purchase.getId());
			this.deferredResults.remove(purchase.getId());
			result.setResult(purchase);
		}
	}
	
}
