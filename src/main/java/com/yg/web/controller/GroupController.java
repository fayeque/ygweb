package com.yg.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yg.web.dto.expenseDto.CreateExpenseDto;
import com.yg.web.dto.groupDto.AddContributionDto;
import com.yg.web.dto.groupDto.GroupDto;
import com.yg.web.dto.groupDto.RoleDto;
import com.yg.web.dto.responses.GroupMembersDtoResponse;
import com.yg.web.dto.responses.GroupsAllDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.entity.Group;
import com.yg.web.repository.GroupRepository;
import com.yg.web.service.GroupService;

@RestController
@RequestMapping("/api/group")
public class GroupController {
	
	@Autowired
	GroupService groupService;
	
	@PostMapping("/addGroup")
	public ResponseEntity<SuccessResponseDto<Object>> addGroup(@RequestBody GroupDto groupDto ){
		return groupService.addGroup(groupDto);
	}
	
	@GetMapping("/getGroupMembers/{groupId}/{year}")
	public ResponseEntity<SuccessResponseDto<GroupMembersDtoResponse>> getGroupMembers(@PathVariable("groupId") Long groupId,
			@PathVariable("year") int year){
		System.out.println("Adding log to debug");
		return groupService.getGroupMembers(groupId,year);
	}
	
	@GetMapping("/getGroupMembersForAdmin/{groupId}/{year}")
	public ResponseEntity<SuccessResponseDto<GroupMembersDtoResponse>> getGroupMembersForAdmin(@PathVariable("groupId") Long groupId,
			@PathVariable("year") int year){
		System.out.println("Adding log to debug");
		return groupService.getGroupMembersForAdmin(groupId,year);
	}
	
	@PostMapping("/addRoleToGroup")
	public ResponseEntity<SuccessResponseDto<Object>> addRoleToGroup(@RequestBody RoleDto roleDto){
		return groupService.addRoleToGroup(roleDto);
	}
	
	@GetMapping("/getGroups")
	public ResponseEntity<SuccessResponseDto<List<GroupsAllDtoResponse>>> getGroups(){
		System.out.println("Inside getGroups");
		return groupService.getGroups();
	}
	
	@PostMapping("/addcontribution")
	public ResponseEntity<SuccessResponseDto<Object>> addContribution(@RequestBody AddContributionDto addContributionDto){
		return groupService.addContribution(addContributionDto);
	}
	
	
	

}
