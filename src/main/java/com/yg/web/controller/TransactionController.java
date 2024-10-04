package com.yg.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.dto.transactionDto.TransactionDto;
import com.yg.web.service.TransactionService;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	
	@Autowired
	TransactionService transactionService;
	
	@PostMapping("/addTransactionForMember")
	public ResponseEntity<SuccessResponseDto<Object>> addTransactionForMember(@RequestBody TransactionDto transactionDto){
			return transactionService.addTransactionForMember(transactionDto);
		
	}
	
	@GetMapping("/undoTransactionForMember/{transactionId}/{groupId}")
	public ResponseEntity<SuccessResponseDto<Object>> undoTransactionForMember(
			@PathVariable("transactionId") Long transactionId,
			@PathVariable("groupId") Long groupId){
			return transactionService.undoTransactionForMember(transactionId,groupId);
		
	}
	

}
