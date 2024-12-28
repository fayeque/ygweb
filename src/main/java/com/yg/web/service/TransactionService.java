package com.yg.web.service;

import java.sql.Date;
import java.util.Optional;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.dto.transactionDto.TransactionDto;
import com.yg.web.entity.Group;
import com.yg.web.entity.Member;
import com.yg.web.entity.Role;
import com.yg.web.entity.Transaction;
import com.yg.web.exceptions.GenericException;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.GroupRepository;
import com.yg.web.repository.MemberRepository;
import com.yg.web.repository.RoleRepository;
import com.yg.web.repository.TransactionRepository;
import com.yg.web.repository.UserRepository;
import com.yg.web.service.producerServices.CreateTransactionMessage;
import com.yg.web.service.producerServices.KafkaProducerFactory;
import com.yg.web.service.producerServices.ProducerService;
import com.yg.web.service.utils.BuildResponseUtils;
import com.yg.web.service.utils.RoleService;
import com.yg.web.utils.SecurityUtils;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final RoleRepository roleRepository;
    
    @Autowired
    RoleService roleService;
    @Autowired
    BuildResponseUtils buildResponseUtils;
    
    @Autowired
    KafkaProducerFactory kafkaProducerFactory;
    
    private Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    public TransactionService(GroupRepository groupRepository, MemberRepository memberRepository, TransactionRepository transactionRepository
    		,RoleRepository roleRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public ResponseEntity<SuccessResponseDto<Object>> addTransactionForMember(TransactionDto transactionDto) {
    	
    	System.out.println("Username here is " + SecurityUtils.getUsername());
    	System.out.println("Username here is " + transactionDto.getGroupId());
    	System.out.println("transactionDto here is "  + transactionDto);
    	
    	if(!roleService.hasRole(transactionDto.getGroupId(),SecurityUtils.getUsername())) {
    		throw new GenericException("User does not have the permission");
    	}
    	
    	Member member = getMemberByUsername(transactionDto.getUsername());
    	
    	Long isExist = memberRepository.findMemberInGroup(member.getId(), transactionDto.getGroupId());
    	
    	
    	logger.info("isExist here is",isExist);
    	logger.info("transactionDto here is " + transactionDto.getUsername());
    	logger.info("transactionDto here is " + transactionDto.getTransactionPeriod());
    	logger.info("transactionDto here is " + transactionDto.getYear());
    	
    	System.out.println("isExist here is " + isExist);
    	
    	if(isExist == 0) {
    		throw new GenericException("Member did not exist in the group");
    	}
    	
    	
        Group group = getGroupById(transactionDto.getGroupId());
        
        if(transactionDto.getTransactionId() != 0) {
        	System.out.println("Coming into transactionId not 0");
        	Transaction transaction = transactionRepository.findById(transactionDto.getTransactionId());
        	group.setTotalAmount(group.getTotalAmount() - transaction.getAmount());
        	Integer id = transactionRepository.deleteById(transactionDto.getTransactionId());
        	groupRepository.save(group);
        }
        
        if(transactionDto.getStatus().equals("REVERT")) {
        	System.out.println("coming into revert");
        	return buildResponseUtils.buildSuccessResponseDto("Transaction deleted sucessfully", null);
        }
        
        Transaction transaction = buildTransaction(transactionDto, group, member);
        transactionRepository.save(transaction);
        
        group.setTotalAmount(transactionDto.getAmount() + group.getTotalAmount());
        groupRepository.save(group);
        
//        ProducerService transactionProducerService = kafkaProducerFactory.getProducer("Transaction");
//        
//        transactionProducerService.sendMessage("CreateTransaction", new CreateTransactionMessage(member.getName(),
//        		member.getUsername(),transactionDto.getYear(),group.getColletionAmount(),transactionDto.getTransactionPeriod()));
        
        return buildResponseUtils.buildSuccessResponseDto("Transaction saved successfully", null);
    }

    private Group getGroupById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group not found"));
    }

    private Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("member not found"));
    }

    private Transaction buildTransaction(TransactionDto transactionDto, Group group, Member member) {
        Transaction transaction = new Transaction();
        transaction.setMember(member);
        transaction.setUser_group(group);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionType(group.getCollectionType());
        transaction.setTransactionPeriod(transactionDto.getTransactionPeriod());
        transaction.setYear(transactionDto.getYear());
        transaction.setAddedBy(SecurityUtils.getUsername());
        transaction.setStatus(transactionDto.getStatus());
        return transaction;
    }

    @Transactional
	public ResponseEntity<SuccessResponseDto<Object>> undoTransactionForMember(Long transactionId,Long groupId) {
    	
    	if(!roleService.hasRole(groupId,SecurityUtils.getUsername())) {
    		throw new GenericException("User does not have the permission");
    	}
		
		Integer id = transactionRepository.deleteById(transactionId);
		
		Optional<Group> group = groupRepository.findById(groupId);
		
	    if(group.isPresent()) {
	    	group.get().setTotalAmount(group.get().getTotalAmount() - group.get().getColletionAmount());
	    	groupRepository.save(group.get());
	    }
		
	    return buildResponseUtils.buildSuccessResponseDto("Transaction deleted sucessfully", null);
	}
    
  
    
    

    
    
    
    
}

