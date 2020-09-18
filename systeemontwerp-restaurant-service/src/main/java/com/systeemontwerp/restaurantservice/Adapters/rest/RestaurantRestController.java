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

import com.systeemontwerp.restaurantservice.Domain.RestaurantService;
import com.systeemontwerp.restaurantservice.Persistence.MenuItemRepository;

@RestController
@RequestMapping(path="/api/restaurant")
@CrossOrigin(origins="*")
public class RestaurantRestController {
	
	private RestaurantService restaurantService;
	
	@Autowired
	public RestaurantRestController(MenuItemRestController menuItemRestController, PurchaseRestController purchaseRestController, RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}
	
	@GetMapping("")
	public String getIndex() {
		
		StringBuilder site = new StringBuilder();
		
		site.append("<html>");
		site.append("<head></head>");
		site.append("<body>");
		
		site.append("<h1>Welcome to the RestaurantPage!</h1>");
		
		site.append("<h3>With this service it's possible to view the menu of today and purchase some menu-items</h3>");
		
		site.append("<h2>View menu items</h2>");
		site.append("<a href=\"/api/restaurant/menuitems\">Show me the menu items</a><br>");
		site.append("<p>To view specific item -> get /menuitems/{item-id}</p>");
		site.append("<a href=\"/api/restaurant/menuitems/menu\">Show me the menu</a><br>");

		site.append("<h2>View purchases</h2>");
		site.append("<a href=\"/api/restaurant/purchases\">Show me the purchases</a><br>");
		
		site.append("</body>");
		site.append("</html>");
		
		return site.toString();
	}
	
}
