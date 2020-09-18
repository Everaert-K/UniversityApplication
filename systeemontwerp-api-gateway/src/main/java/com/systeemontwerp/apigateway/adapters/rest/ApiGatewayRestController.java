package com.systeemontwerp.apigateway.adapters.rest;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGatewayRestController {
	
	private final Logger logger = Logger.getLogger(ApiGatewayRestController.class);

	//index page
	//The general html page
		@GetMapping("")
		public String showIndex() {
			StringBuilder site = new StringBuilder();
			site.append("<html>");
			site.append("<head></head>");
			site.append("<body>");
			site.append("<h1>Welcome to the University Application!</h1>");
			site.append("<h2><a href=\"/api/professors\">The Professor Service</h2>");
			site.append("<h2><a href=\"/api/students\">The Student Service</h2>");
			site.append("<h2><a href=\"/api/courses\">The Cursus Service</h2>");
			site.append("<h2><a href=\"/api/restaurant\">The Restaurant Service</h2>");
			site.append("<h2><a href=\"/api/financial\">The Financial Service</h2>");
			site.append("<h2><a href=\"/api/research\">The Onderzoek Service</h2>");
			site.append("<h2><a href=\"/api/rooster\">The Rooster Service</h2>");
			site.append("<h2><a href=\"/api/reservering\">The Reservering Service</h2>");
			site.append("</body>");
			site.append("</html>");
			return site.toString();
		}
}
