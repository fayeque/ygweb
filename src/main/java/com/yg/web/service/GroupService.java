package com.yg.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.boot.spi.AdditionalMappingContributions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yg.web.dto.dbToObjMapDto.MemberTransactionDTO;
import com.yg.web.dto.groupDto.AddContributionDto;
import com.yg.web.dto.groupDto.GroupDto;
import com.yg.web.dto.groupDto.RoleDto;
import com.yg.web.dto.responses.GroupDtoResponse;
import com.yg.web.dto.responses.GroupMembersDtoResponse;
import com.yg.web.dto.responses.GroupsAllDtoResponse;
import com.yg.web.dto.responses.MemberDtoResponse;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.dto.responses.TransactionDtoResponse;
import com.yg.web.entity.Contribution;
import com.yg.web.entity.Expense;
import com.yg.web.entity.Group;
import com.yg.web.entity.Member;
import com.yg.web.entity.Role;
import com.yg.web.entity.Transaction;
import com.yg.web.entity.User;
import com.yg.web.exceptions.GenericException;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.ContributionRepository;
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
	ContributionRepository contributionRepository;
	
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
	
	public ResponseEntity<SuccessResponseDto<GroupMembersDtoResponse>> getGroupMembers(Long groupId,int year){
		return getGenericGroupMembers(groupId,year);
		
	}
	
	public ResponseEntity<SuccessResponseDto<GroupMembersDtoResponse>> getGroupMembersForAdmin(Long groupId,int year){
		String username = SecurityUtils.getUsername();
		Optional<Role> roleCount = Optional.ofNullable(roleRepository.findByGroupAndUsername(groupId, username));
		System.out.println("username " + username + "groupId " + groupId);
		System.out.println("roleCount.isPresent() " + roleCount.isPresent());
		if(!roleCount.isPresent()) {
			throw new GenericException("You are allowed to perform the operation");
		}
		
		return getGenericGroupMembers(groupId,year);
		
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
	
	
	public ResponseEntity<SuccessResponseDto<List<GroupsAllDtoResponse>>> getGroups() {
		
		List<Group> groups= groupRepository.findAllWithMembers();
	    List<GroupsAllDtoResponse> groupsAllDtoResponse = groups.stream()
		        .map((group) -> convertToGroupAllDto(group))
		        .collect(Collectors.toList());
//	    System.out.println(groupsAllDtoResponse.get(0));
		return buildResponseUtils.buildSuccessResponseDto("Group fetched", groupsAllDtoResponse);
	}
	
	public ResponseEntity<SuccessResponseDto<Object>> addContribution(AddContributionDto addContributionDto) {
		
		System.out.println("Inside expense service");
		
		Role role =Optional.ofNullable(roleRepository.findByGroupAndUsername(addContributionDto.getGroupId(), SecurityUtils.getUsername()))
				.orElseThrow(() -> new ResourceNotFoundException("User does not have the required Permission"));
		
		User user = userRepository.findByUsername(SecurityUtils.getUsername());
		
		Contribution contribution = new Contribution();
		contribution.setDescription(addContributionDto.getDescription());
		contribution.setAmount(addContributionDto.getAmount());
		contribution.setCreatedBy(user);
		contribution.setGroupId(addContributionDto.getGroupId());
		
		contributionRepository.save(contribution);
		
		Group group = groupRepository.findById(addContributionDto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
		group.setTotalAmount(group.getTotalAmount() + addContributionDto.getAmount());
		groupRepository.save(group);
		
		return buildResponseUtils.buildSuccessResponseDto("Contribution added Successfully", null);
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
	
	// Convert Group to AllGroupsDto
	private GroupsAllDtoResponse convertToGroupAllDto(Group group) {
		GroupsAllDtoResponse groupsAllDtoResponse= new GroupsAllDtoResponse();
		groupsAllDtoResponse.setGroupId(group.getGroupId());
		groupsAllDtoResponse.setGroupName(group.getGroupName());
		groupsAllDtoResponse.setCollectionAmount(group.getColletionAmount());
		groupsAllDtoResponse.setCollectionType(group.getCollectionType());
		groupsAllDtoResponse.setTotalAmount(group.getTotalAmount());
		groupsAllDtoResponse.setCreatedAt(group.getCreatedAt());
		System.out.println("Finding queries framed here");
		int memberSize = group.getMembers().size();
		System.out.println("member size here is " + memberSize);
		groupsAllDtoResponse.setTotalMembers(memberSize);
	    return groupsAllDtoResponse;
	}
	
	public ResponseEntity<SuccessResponseDto<GroupMembersDtoResponse>> getGenericGroupMembers(Long groupId,int year){
Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
		
		List<Transaction> transactions= transactionRepository.findByUserGroup(group,year);
				
		
		GroupMembersDtoResponse groupDTO = new GroupMembersDtoResponse();
        groupDTO.setGroupId(groupId);
        groupDTO.setGroupName(group.getGroupName());
        groupDTO.setAmount(group.getColletionAmount());
        groupDTO.setYear(year);
        groupDTO.setTotalAmount(group.getTotalAmount());
        
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
            transactionDTO.setStatus(transaction.getStatus());
            
            // Add transaction to the member's transaction list
            memberDTO.getTransactionDtoResponse().add(transactionDTO);
        }

        // Add members to the group DTO
        groupDTO.setMemberDtoResponses(new ArrayList<>(memberMap.values()));
		
		

        return buildResponseUtils.buildSuccessResponseDto("Group members data", groupDTO);
	}


	




}
