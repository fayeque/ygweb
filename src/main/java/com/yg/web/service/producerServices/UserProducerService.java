package com.yg.web.service.producerServices;

import org.mapstruct.ap.shaded.freemarker.core.ReturnInstruction.Return;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserProducerService extends ProducerService {


    public UserProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
		super(kafkaTemplate);
	}    
    
   
	public void createUser(Object message) {
		System.out.println("Inside createUser func in UserProducerService");
    	publishMessageToTopic("CreateUser",message);
    }
	
	public void deleteUser(Object message) {
		System.out.println("Inside deleteUser func in UserProducerService");
		publishMessageToTopic("DeleteUser",message);
	}


	@Override
	public void sendMessage(String topic, Object message) {
		System.out.println("Method being called here " + topic + "with message " + message);
		switch (topic) {
		case "CreateUser": {
			if(message instanceof CreateUserMessage) 
				createUser(message);
			
			break;
		}
		case "DeleteUser": {
			if(message instanceof DeleteUserMessage) 
			deleteUser(message);
			break;
		}
        default:
            System.out.println("Unknown topic: " + topic);
	}
	}


}
