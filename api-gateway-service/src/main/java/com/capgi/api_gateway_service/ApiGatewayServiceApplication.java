package com.capgi.api_gateway_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient //to register with the eureka
@SpringBootApplication
public class ApiGatewayServiceApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiGatewayServiceApplication.class);

	public static void main(String[] args) {
		
		logger.info("Starting API Gateway Service...");
        SpringApplication.run(ApiGatewayServiceApplication.class, args);
        logger.info("API Gateway Service started successfully.");
	}

}
