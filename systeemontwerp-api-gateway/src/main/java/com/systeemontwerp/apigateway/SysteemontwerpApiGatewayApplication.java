package com.systeemontwerp.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SysteemontwerpApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SysteemontwerpApiGatewayApplication.class, args);
	}
	
	@Bean
	public RouteLocator schoolAppRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.host("*").and().path("/api/financial/**").uri("http://financialservice:2221"))
				.route(r -> r.host("*").and().path("/api/courses/**").uri("http://cursusservice:2220"))
				.route(r -> r.host("*").and().path("/api/restaurant/**").uri("http://restaurantservice:2226"))
				.route(r -> r.host("*").and().path("/api/students/**").uri("http://studentservice:2222"))
				.route(r -> r.host("*").and().path("/api/professors/**").uri("http://professorservice:2223"))
				.route(r -> r.host("*").and().path("/api/rooster/**").uri("http://roosterservice:2224"))
				.route(r -> r.host("*").and().path("/api/reservering/**").uri("http://reserveringservice:2225"))
				.route(r -> r.host("*").and().path("/api/research/**").uri("http://onderzoekservice:2227"))
				.build();
	}

}
