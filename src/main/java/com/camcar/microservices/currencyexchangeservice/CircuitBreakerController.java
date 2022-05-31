package com.camcar.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api-retry")
	@Retry(name ="sample-api-retry", fallbackMethod = "hardcodedResponseRetry")
	public String sampleApiRetry() {
		
		logger.info("Sample API call received");
		
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-api-circuit")
	@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponseCircuit")
	public String sampleApiCircuitBreaker() {
		
		logger.info("Sample API call received circuit method normal");
		
		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url", String.class);
		return forEntity.getBody();
	}
	
	@GetMapping("/sample-api-limiter")
	@RateLimiter(name="default", fallbackMethod = "hardcodedResponseLimiter")
	public String sampleApiLimiter() {
		
		logger.info("Sample API call received-limiter");
		return "sample-api-limiter";
	}
	
	@GetMapping("/sample-api-bulk")
	@Bulkhead(name ="sample-api-bulk")
	public String sampleApi() {
		
		logger.info("Sample API call received bulk");
		return "sample-api-bulk";
	}
	
	public String hardcodedResponseRetry(Exception e) {
		return "fallback-response-retry";
	}
	
	public String hardcodedResponseLimiter(Exception e) {
		return "fallback-response-limiter";
	}
	
	public String hardcodedResponseCircuit(Exception e) {
		logger.info("Sample API call received circuit fallbackmethod");
		return "fallback-response-circuit-breaker";
	}

}