package com.yg.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yg.web.dto.expenseDto.CreateExpenseDto;
import com.yg.web.dto.groupDto.GroupDto;
import com.yg.web.dto.responses.GroupExpenseDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.service.ExpenseService;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {
	
	@Autowired
	ExpenseService expenseService;

	@PostMapping("/addExpense")
	public ResponseEntity<SuccessResponseDto<Object>> addExpense(@RequestBody CreateExpenseDto createExpenseDto){
		return expenseService.addExpense(createExpenseDto);
	}
	
	@GetMapping("/undoExpense/{expenseId}")
	public ResponseEntity<SuccessResponseDto<Object>> undoExpense(@PathVariable("expenseId") Long expenseId){
		return expenseService.undoExpense(expenseId);
	}
	
	
	@GetMapping("/getExpense/{groupId}")
	public ResponseEntity<SuccessResponseDto<GroupExpenseDtoResponse>> getExpense(@PathVariable("groupId") Long groupId){
		return expenseService.getExpense(groupId);
	}
	
	

}
