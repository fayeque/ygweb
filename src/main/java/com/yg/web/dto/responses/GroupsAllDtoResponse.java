package com.yg.web.dto.responses;


import java.time.LocalDateTime;
import java.util.List;

import com.yg.web.enums.CollectionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsAllDtoResponse {
	public Long groupId;
	public String groupName;
	public CollectionType collectionType;
	public int collectionAmount;
	public LocalDateTime createdAt;
	public int totalAmount;
	public int totalMembers;

}
