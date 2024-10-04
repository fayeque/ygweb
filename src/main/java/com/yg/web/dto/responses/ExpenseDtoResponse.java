package com.yg.web.dto.responses;

import java.time.LocalDateTime;

import com.yg.web.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDtoResponse {
	
	private String description;
	private int amount;
	private LocalDateTime createdAt;
	private String createdByUsername;
	private String createdByName;
 	
	 

}
