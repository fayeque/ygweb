package com.yg.web.service.producerServices;

import org.apache.kafka.common.network.Send;
import org.bouncycastle.jcajce.util.MessageDigestUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

public abstract class ProducerService {
	
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public abstract void sendMessage(String topic,Object message);
    
    public void publishMessageToTopic(String topicDetail,Object message) {
    	System.out.println("Sending messge to kafka on topic " + topicDetail);
//    	Message<Object> message2 = MessageBuilder.withPayload(message).setHeader(KafkaHeaders.TOPIC, "CreateUserJson").build();
    	  kafkaTemplate.send(topicDetail, message);
    }
    

}
