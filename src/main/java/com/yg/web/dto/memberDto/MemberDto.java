package com.yg.web.dto.memberDto;

import com.yg.web.entity.Group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
	
	private String name; 
	private String username;
	private Long groupId;

}
