package com.yg.web.dto.groupDto;

import com.yg.web.enums.CollectionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
	private String groupName;
	private CollectionType collectionType;
	private Integer collectionAmount;
}
