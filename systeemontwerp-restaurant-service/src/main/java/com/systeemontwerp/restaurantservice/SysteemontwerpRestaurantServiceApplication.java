package com.systeemontwerp.restaurantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.systeemontwerp.restaurantservice.Adapters.messaging.MessagingChannels;
import com.systeemontwerp.restaurantservice.Adapters.messaging.RestaurantMessageGateway;
import com.systeemontwerp.restaurantservice.Domain.PaymentMethod;
import com.systeemontwerp.restaurantservice.Domain.Type;
import com.systeemontwerp.restaurantservice.Domain.MenuItem;
import com.systeemontwerp.restaurantservice.Domain.Purchase;
import com.systeemontwerp.restaurantservice.Domain.PurchaseStatus;
import com.systeemontwerp.restaurantservice.Persistence.MenuItemRepository;
import com.systeemontwerp.restaurantservice.Persistence.PurchaseRepository;
//
@SpringBootApplication
@EnableBinding(MessagingChannels.class)
public class SysteemontwerpRestaurantServiceApplication {

	Logger logger = LoggerFactory.getLogger(SysteemontwerpRestaurantServiceApplication.class);
	private List<MenuItem> testitems;
	
	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpRestaurantServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner populateMenuDb(MenuItemRepository repository) {
		
		return (args) -> {
			//If we have an empty database, populate it
			this.testitems = new ArrayList<MenuItem>();
			if(repository.count() == 0) {
				logger.info("Populating the database...");
				MenuItem a = new MenuItem("Potatoes", 1000, 1.30, false, Type.ZETMEEL);
				MenuItem b = new MenuItem("Fries", 1000, 1.20, true, Type.ZETMEEL);
				MenuItem c = new MenuItem("Steak", 1000, 1.50, true, Type.PROTEIN);
				MenuItem d = new MenuItem("Granola", 1000, 0.90, false, Type.PROTEIN);
				MenuItem e = new MenuItem("Ketchup", 1000, 0.60, false, Type.SAUS);
				MenuItem f = new MenuItem("Ham", 1000, 1.30, false, Type.BROODJE);
				MenuItem g = new MenuItem("Ham/Cheese", 1000, 1.30, false, Type.BROODJE);
				this.testitems.add(a);
				this.testitems.add(b);
				this.testitems.add(c);
				this.testitems.add(d);
				this.testitems.add(e);
				this.testitems.add(f);
				this.testitems.add(g);
				repository.saveAll(this.testitems);
			}
			logger.info("Amount of menuitems in the database: " + repository.count());
		};
	}
	
	@Bean
	public CommandLineRunner populatePurchaseDb(PurchaseRepository repository) {
		
		return (args) -> {
			//If we have an empty database, populate it
			if(repository.count() == 0) {
				logger.info("Populating the database...");
				HashMap<MenuItem, Integer> order = new HashMap<>();
				order.put(this.testitems.get(0), 1);
				order.put(this.testitems.get(2), 1);
				order.put(this.testitems.get(4), 2);
				Purchase purchase = new Purchase("karel", "vince", order,true,PaymentMethod.BANCONTACT);
				repository.save(purchase);
			}
			logger.info("Amount of purchases in the database: " + repository.count());
		};
	}
	
	/*
	@Bean
	public CommandLineRunner populateDb(MenuItemRepository repository) {
		
		return (args) -> {
			logger.info("Emptying database...");
			repository.deleteAll();
			
			logger.info("Start populating...");
			MenuItem r = new MenuItem("kip", 7, 3, false, Type.PROTEIN);
			MenuItem r2 = new MenuItem("frieten", 1000, 4, true, Type.ZETMEEL);
			MenuItem r3 = new MenuItem("sandwich",12,1,false,Type.BROODJE);
			MenuItem r4 = new MenuItem("kaassaus", 31, 3, true, Type.SAUS);
			repository.save(r);
			repository.save(r2);
			repository.save(r3);
			repository.save(r4);
		};
	}
	
	@Bean
	public CommandLineRunner testRepositoryMethods(MenuItemRepository repository) {
		return (args) -> {
			logger.info("Showing all menu items:");
			repository.findAll().forEach((menuItem) -> logger.info(menuItem.toString()));
		};
	}
	
	@Bean
	public CommandLineRunner testMessagingGateway(PurchaseRepository repository, RestaurantMessageGateway gateway) {
		return (args) ->{
			logger.info("testMessagingGateway");
			logger.info("Emptying database...");
			repository.deleteAll();
			
			logger.info("Start populating...");
			// String from, String to,HashMap<menuItem,Integer> aankopen,boolean korting,purchaseStatus status
			
			HashMap<MenuItem,Integer> aankopen = new HashMap<MenuItem,Integer>();
			MenuItem r1 = new MenuItem("kip", 7, 3, false, Type.PROTEIN);
			MenuItem r2 = new MenuItem("frieten", 1000, 4, true, Type.ZETMEEL);
			MenuItem r3 = new MenuItem("sandwich",12,1,false,Type.BROODJE);
			MenuItem r4 = new MenuItem("kaassaus", 31, 3, true, Type.SAUS);
			aankopen.put(r1, 1);
			aankopen.put(r2, 12);
			aankopen.put(r3, 6);
			aankopen.put(r4, 24);
			
			
			Purchase p1 = new Purchase("karel","ugent", aankopen,false,PaymentMethod.EPURSE);
			Purchase p2 = new Purchase("Vince","ugent",aankopen,true,PaymentMethod.BANCONTACT);
			Purchase p3 = new Purchase("robbe","ugent",aankopen,true,PaymentMethod.BANCONTACT);
			Purchase p4 = new Purchase("dylan","ugent",aankopen,true,PaymentMethod.EPURSE);
			repository.save(p1);
			repository.save(p2);
			repository.save(p3);
			repository.save(p4);
			
			Purchase p = repository.getPurchasesFrom("karel").get(0);
			
			// gateway.askPayment(p); // kafka message broker zeker aanzetten voor je dit wilt testen
			// zie https://kafka.apache.org/quickstart

		};
	}
	*/

}
