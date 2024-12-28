package com.yg.web.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yg.web.dto.memberDto.DeleteMemberDto;
import com.yg.web.dto.memberDto.EditMemberDto;
import com.yg.web.dto.memberDto.MemberDto;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.entity.Group;
import com.yg.web.entity.Member;
import com.yg.web.entity.User;
import com.yg.web.exceptions.GenericException;
import com.yg.web.exceptions.ResourceNotFoundException;
import com.yg.web.repository.GroupRepository;
import com.yg.web.repository.MemberRepository;
import com.yg.web.service.utils.BuildResponseUtils;

@Service
public class MemberService {

	@Autowired
	MemberRepository memberRepository;
	@Autowired
	GroupRepository groupRepository;
	@Autowired
	BuildResponseUtils buildResponseUtils;
	
	public ResponseEntity<SuccessResponseDto<Object>> addMember(MemberDto memberDto){
		
		Group group = groupRepository.findById(memberDto.getGroupId()).orElseThrow(() -> new ResourceNotFoundException("group not found"));
		
		Member member= memberRepository.findByUsername(memberDto.getUsername()).orElse(null);
		
		if(member == null) {
			member = new Member();
			member.setName(memberDto.getName());
			member.setUsername(memberDto.getUsername());
			member.setEmailId(memberDto.getEmailId());
			 member = memberRepository.save(member);
		}
		
		if(!group.getMembers().contains(member)) {
			group.getMembers().add(member);
			groupRepository.save(group);
		}else {
			throw new GenericException("Member with the given username already exist");
		}
		
		return buildResponseUtils.buildSuccessResponseDto("Member added successfully", null);
	}

	public ResponseEntity<SuccessResponseDto<Object>> editMember(EditMemberDto memberDto) {
		
		Member member = memberRepository.findByUsername(memberDto.getUsername()).orElseThrow(() -> new RuntimeException("Member not found"));
		member.setName(memberDto.getName());
		memberRepository.save(member);
		return buildResponseUtils.buildSuccessResponseDto("Member updated successdully", null);
	}

	public ResponseEntity<SuccessResponseDto<Object>> deleteMember(DeleteMemberDto deleteMemberDto) {
        Group group = groupRepository.findById(deleteMemberDto.getGroupId()).orElseThrow(() -> new RuntimeException("Group not found with id " + deleteMemberDto.getGroupId()));

            Member member = memberRepository.findByUsername(deleteMemberDto.getUsername())
                .orElseThrow(() -> new RuntimeException("Member not found with id " + deleteMemberDto.getUsername()));

            group.getMembers().remove(member);
            groupRepository.save(group);
            return buildResponseUtils.buildSuccessResponseDto("Member deleted successfully from the group", null);
	}
}
