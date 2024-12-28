package com.yg.web.service.producerServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerFactory {
	
	private final UserProducerService userProducerService;
	private final TransactionProducerService transactionProducerService;
	
	@Autowired
   public KafkaProducerFactory(UserProducerService userProducerService,TransactionProducerService transactionCreatedProducerService) {
	this.userProducerService = userProducerService;
	this.transactionProducerService = transactionCreatedProducerService;
	   
   }
	
	public ProducerService getProducer(String producerType) {
		
		switch (producerType) {
        case "User":
            return userProducerService;
        case "Transaction":
            return transactionProducerService;
        default:
            throw new IllegalArgumentException("Unknown producer type: " + producerType);
    }
		
	}
	

}
