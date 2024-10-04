package com.yg.web.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yg.web.dto.expenseDto.CreateExpenseDto;
import com.yg.web.dto.groupDto.GroupDto;
import com.yg.web.dto.responses.ExpenseDtoResponse;
import com.yg.web.dto.responses.GroupExpenseDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.entity.Expense;
import com.yg.web.entity.Group;
import com.yg.web.entity.Role;
import com.yg.web.entity.User;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.ExpenseRepository;
import com.yg.web.repository.GroupRepository;
import com.yg.web.repository.RoleRepository;
import com.yg.web.repository.UserRepository;
import com.yg.web.service.utils.BuildResponseUtils;
import com.yg.web.utils.SecurityUtils;

@Service
public class ExpenseService {
	
	@Autowired
	ExpenseRepository expenseRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	BuildResponseUtils buildResponseUtils;
	
	public ResponseEntity<SuccessResponseDto<Object>> addExpense(CreateExpenseDto createExpenseDto){
		
		System.out.println("Inside expense service");
		
		Role role =Optional.ofNullable(roleRepository.findByGroupAndUsername(createExpenseDto.getGroupId(), SecurityUtils.getUsername()))
				.orElseThrow(() -> new ResourceNotFoundException("User does not have the required Permission"));
		
		User user = userRepository.findByUsername(SecurityUtils.getUsername());
		
		Expense expense = new Expense();
		expense.setDescription(createExpenseDto.getDescription());
		expense.setAmount(createExpenseDto.getAmount());
		expense.setCreatedBy(user);
		expense.setGroupId(createExpenseDto.getGroupId());
		
		expenseRepository.save(expense);
		
		Group group = groupRepository.findById(createExpenseDto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
		group.setTotalAmount(group.getTotalAmount() - createExpenseDto.getAmount());
		groupRepository.save(group);
		
		return buildResponseUtils.buildSuccessResponseDto("Expense added Successfully", null);
		
	}

	public ResponseEntity<SuccessResponseDto<GroupExpenseDtoResponse>> getExpense(Long groupId) {
		Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
		List<Expense> expenses =  expenseRepository.findByGroupId(groupId);
		return buildResponseUtils.buildSuccessResponseDto("Expense added Successfully", convertToExpenseResponseDto(expenses,group));

	}
	
	public ResponseEntity<SuccessResponseDto<Object>> undoExpense(Long expenseId) {
		
		Expense expense = expenseRepository.getById(expenseId);
		Role role =Optional.ofNullable(roleRepository.findByGroupAndUsername(expense.getGroupId(), SecurityUtils.getUsername()))
				.orElseThrow(() -> new ResourceNotFoundException("User does not have the permission"));
		Optional<Group> group = groupRepository.findById(expense.getGroupId());
		group.get().setTotalAmount(group.get().getTotalAmount() + expense.getAmount());
		groupRepository.save(group.get());
		expenseRepository.deleteById(expenseId);
		
		return buildResponseUtils.buildSuccessResponseDto("Expenses deleted successfully", null);
	}

	private GroupExpenseDtoResponse convertToExpenseResponseDto(List<Expense> expenses,Group group) {
		
		GroupExpenseDtoResponse groupExpenseDtoResponse = new GroupExpenseDtoResponse();
		groupExpenseDtoResponse.setGroupId(group.getGroupId());
		groupExpenseDtoResponse.setGroupName(group.getGroupName());
		
		List<ExpenseDtoResponse> result = expenses.stream().map(this::convertToExpenseDto).collect(Collectors.toList());
		
		groupExpenseDtoResponse.setExpenseDtoResponses(result);
		return groupExpenseDtoResponse;
		
	}
	
	private ExpenseDtoResponse convertToExpenseDto(Expense expense) {
		ExpenseDtoResponse expenseDtoResponse = new ExpenseDtoResponse();
		expenseDtoResponse.setAmount(expense.getAmount());
		expenseDtoResponse.setDescription(expense.getDescription());
		expenseDtoResponse.setCreatedAt(expense.getCreatedAt());
		expenseDtoResponse.setCreatedByName(expense.getCreatedBy().getName());
		expenseDtoResponse.setCreatedByUsername(expense.getCreatedBy().getUsername());
		
		return expenseDtoResponse;
	}



}
