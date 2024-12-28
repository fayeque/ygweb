package com.yg.web.service.producerServices;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserMessage {
	
	public String userName;
	public String emailId;

}
