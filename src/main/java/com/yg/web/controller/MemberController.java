package com.yg.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yg.web.dto.memberDto.DeleteMemberDto;
import com.yg.web.dto.memberDto.EditMemberDto;
import com.yg.web.dto.memberDto.MemberDto;
import com.yg.web.dto.responses.SuccessResponseDto;
import com.yg.web.repository.MemberRepository;
import com.yg.web.service.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	
	@PostMapping("/addMember")
	public ResponseEntity<SuccessResponseDto<Object>> addMember(@RequestBody MemberDto memberDto){	
		return memberService.addMember(memberDto);	
	}
	
	@PostMapping("/editMember")
	public ResponseEntity<SuccessResponseDto<Object>> editMember(@RequestBody EditMemberDto memberDto){	
		return memberService.editMember(memberDto);	
	}
	
	@PostMapping("/deleteMember")
	public ResponseEntity<SuccessResponseDto<Object>> deleteMember(@RequestBody DeleteMemberDto deleteMemberDto){	
		return memberService.deleteMember(deleteMemberDto);	
	}
	

}
