package com.yg.web.dto.groupDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddContributionDto {
	private String description;
	private int amount;
	private Long groupId;
}
