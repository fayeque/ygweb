package com.yg.web.dto.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupExpenseDtoResponse {

	private Long groupId;
	private String groupName;
	private Integer totalExpense;
	private Integer totalAmount;
	List<ExpenseDtoResponse> expenseDtoResponses = new ArrayList<ExpenseDtoResponse>();
}
