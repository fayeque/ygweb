package com.yg.web.service.producerServices;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class TransactionProducerService extends ProducerService{

	public TransactionProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
		super(kafkaTemplate);
	}

	public void createTransaction(Object message) {
    	publishMessageToTopic("CreateTransaction",message);
    }

	@Override
	public void sendMessage(String topic, Object message) {
		switch (topic) {
		case "CreateTransaction": {
			createTransaction(message);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + topic);
		}
		
	}

}
