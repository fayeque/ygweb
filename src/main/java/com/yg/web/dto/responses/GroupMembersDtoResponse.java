package com.yg.web.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersDtoResponse {
	
	public Long groupId;
	public String groupName;
	public int year;
	public int amount;
	public int totalAmount;
	List<MemberDtoResponse> memberDtoResponses;

}
