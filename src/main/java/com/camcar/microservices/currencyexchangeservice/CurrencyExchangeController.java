package com.camcar.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

	private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
	
	@Autowired
	private Environment environment;

	@Autowired
	private CurrencyExchangeRepository repository;

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
		logger.info("retrieveExchangeValue called with {} to {} ", from, to);
		CurrencyExchange exchangeValue = repository.findByFromAndTo(from, to);
		if (exchangeValue == null) {
			throw new RuntimeException("Unable to find data for " + from + " to " + to);
		}
		String port = environment.getProperty("local.server.port");
		String host = environment.getProperty("HOSTNAME");
		String version = "v2";
		exchangeValue.setEnvironment(port+" "+version+" "+host);
//		exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		return exchangeValue;
	}
}
