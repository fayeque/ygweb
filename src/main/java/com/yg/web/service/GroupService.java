package com.yg.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yg.web.dto.dbToObjMapDto.MemberTransactionDTO;
import com.yg.web.dto.groupDto.GroupDto;
import com.yg.web.dto.groupDto.RoleDto;
import com.yg.web.dto.responses.GroupDtoResponse;
import com.yg.web.dto.responses.GroupMembersDtoResponse;
import com.yg.web.dto.responses.MemberDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.dto.responses.TransactionDtoResponse;
import com.yg.web.entity.Group;
import com.yg.web.entity.Member;
import com.yg.web.entity.Role;
import com.yg.web.entity.Transaction;
import com.yg.web.entity.User;
import com.yg.web.exceptions.GenericException;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.GroupRepository;
import com.yg.web.repository.RoleRepository;
import com.yg.web.repository.TransactionRepository;
import com.yg.web.repository.UserRepository;
import com.yg.web.service.utils.BuildResponseUtils;
import com.yg.web.utils.SecurityUtils;

@Service
public class GroupService {
	
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	TransactionRepository transactionRepository;
	
	@Autowired
	BuildResponseUtils buildResponseUtils;
	
	public ResponseEntity<SuccessResponseDto<Object>> addGroup(GroupDto groupDto){
		
	
		String username = SecurityUtils.getUsername();
		System.out.println("Username fetch here is " + username);
		System.out.println("groupDto.getCollectionAmount() " + groupDto.getCollectionAmount());
		
		User user= userRepository.findByUsername(username);
		
		boolean isGroupUserCombExist = groupRepository.existsByGroupNameAndUser(groupDto.getGroupName(), user);
		System.out.println("isGroupUserCombExist " + isGroupUserCombExist);
		
		if(isGroupUserCombExist) {
			throw new GenericException("Same group name already exist for the user");
		}
		
		
		Group user_group=new Group();
		user_group.setGroupName(groupDto.getGroupName());
		user_group.setUser(user);
		user_group.setCollectionType(groupDto.getCollectionType());
		user_group.setColletionAmount(groupDto.getCollectionAmount());
		
		groupRepository.save(user_group);
		
		buildRole(user_group.getGroupId(),user.getUsername());
		
		
		return buildResponseUtils.buildSuccessResponseDto("Group addedd Successfully", null);
	}
	
	public ResponseEntity<SuccessResponseDto<GroupMembersDtoResponse>> getGroupMembers(Long groupId){
		Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
		
		List<Transaction> transactions= transactionRepository.findByUserGroup(group);
				
		
		GroupMembersDtoResponse groupDTO = new GroupMembersDtoResponse();
        groupDTO.setGroupId(groupId);
        groupDTO.setGroupName(group.getGroupName());
        
        Map<String, MemberDtoResponse> memberMap = new HashMap<>();
        
        for(Member member : group.getMembers()) {
            MemberDtoResponse memberDTO = memberMap.computeIfAbsent(member.getUsername(), id -> {
            	MemberDtoResponse newMemberDTO = new MemberDtoResponse();
            	newMemberDTO.setId(member.getId());
                newMemberDTO.setUsername(member.getUsername());
                newMemberDTO.setName(member.getName());
                newMemberDTO.setTransactionDtoResponse(new ArrayList<>());
                return newMemberDTO;
            });
        }

        for (Transaction transaction : transactions) {
            Member member = transaction.getMember();
            
            // If member is not yet in the map, add it
            MemberDtoResponse memberDTO = memberMap.computeIfAbsent(member.getUsername(), id -> {
            	MemberDtoResponse newMemberDTO = new MemberDtoResponse();
            	newMemberDTO.setId(member.getId());
                newMemberDTO.setUsername(member.getUsername());
                newMemberDTO.setName(member.getName());
                newMemberDTO.setTransactionDtoResponse(new ArrayList<>());
                return newMemberDTO;
            });

            // Map transaction details to TransactionDTO
            TransactionDtoResponse transactionDTO = new TransactionDtoResponse();
            transactionDTO.setId(transaction.getId());
            transactionDTO.setAmount(transaction.getAmount());
            transactionDTO.setTransactionPeriod(transaction.getTransactionPeriod());
            transactionDTO.setTransactionDate(transaction.getTransactionDate());
            transactionDTO.setTransactionType(transaction.getTransactionType());
            transactionDTO.setYear(transaction.getYear());
            
            // Add transaction to the member's transaction list
            memberDTO.getTransactionDtoResponse().add(transactionDTO);
        }

        // Add members to the group DTO
        groupDTO.setMemberDtoResponses(new ArrayList<>(memberMap.values()));
		
		

        return buildResponseUtils.buildSuccessResponseDto("Group members data", groupDTO);
		
		
	}
	


	public ResponseEntity<SuccessResponseDto<Object>> addRoleToGroup(RoleDto roleDto) {
		String username = SecurityUtils.getUsername();
		Group group = groupRepository.findById(roleDto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
		
		System.out.println("group.getUser().getUsername() " + group.getUser().getUsername() + " username " + username);
		System.out.println(!username.equals(group.getUser().getUsername()));
		
		if(!username.equals(group.getUser().getUsername())) {
			throw new GenericException("You are not authorised to add Role to this group");
		}
		
		Optional<Role> roleCount = Optional.ofNullable(roleRepository.findByGroupAndUsername(roleDto.getGroupId(), roleDto.getUsername()));
		System.out.println("roleCount.isPresent() " + roleCount.isPresent());
		if(roleCount.isPresent()) {
			throw new GenericException("User is already admin to group");
		}
		
		buildRole(roleDto.getGroupId(), roleDto.getUsername());
		
		return buildResponseUtils.buildSuccessResponseDto("Admin role given to user for this group",null);
	}
	
	public void buildRole(Long groupId,String username) {
		Role role = new Role();
		role.setGroupId(groupId);
		role.setUsername(username);
		roleRepository.save(role);
	}
	
	// Convert Group to GroupDto
	private GroupMembersDtoResponse convertToGroupDto(Group group) {
		GroupMembersDtoResponse groupMembersDtoResponse = new GroupMembersDtoResponse();
		groupMembersDtoResponse.setGroupId(group.getGroupId());
		groupMembersDtoResponse.setGroupName(group.getGroupName());

	    List<MemberDtoResponse> memberDtoResponse = group.getMembers().stream()
	        .map((member) -> convertToMemberDto(member,group))
	        .collect(Collectors.toList());

	    groupMembersDtoResponse.setMemberDtoResponses(memberDtoResponse);
	    return groupMembersDtoResponse;
	}

	// Convert Member to MemberDto
	private MemberDtoResponse convertToMemberDto(Member member,Group group) {
		MemberDtoResponse memberDto = new MemberDtoResponse();
	    memberDto.setId(member.getId());
	    memberDto.setUsername(member.getUsername());
	    memberDto.setName(member.getName());
	    

	    List<TransactionDtoResponse> transactionDtos = transactionRepository.findByMemberAndUserGroup(member,group).stream()
	        .map(this::convertToTransactionDto)
	        .collect(Collectors.toList());

	    memberDto.setTransactionDtoResponse(transactionDtos);
	    return memberDto;
	}

	// Convert Transaction to TransactionDto
	private TransactionDtoResponse convertToTransactionDto(Transaction transaction) {
		TransactionDtoResponse transactionDto = new TransactionDtoResponse();
	    transactionDto.setId(transaction.getId());
	    transactionDto.setYear(transaction.getYear());
	    transactionDto.setAmount(transaction.getAmount());
	    transactionDto.setTransactionPeriod(transaction.getTransactionPeriod());
	    transactionDto.setTransactionType(transaction.getTransactionType());
	    transactionDto.setTransactionDate(transaction.getTransactionDate());
	    return transactionDto;
	}
	




}
