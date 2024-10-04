package com.yg.web.dto.expenseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExpenseDto {
	
	private String description;
	private int amount;
	private Long groupId;

}
