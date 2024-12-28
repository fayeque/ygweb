package com.yg.web.service.producerServices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionMessage {
		
	public String emailId;
	public String username;
    private Integer year;
    private Integer amount;
    private String transactionPeriod;

}
