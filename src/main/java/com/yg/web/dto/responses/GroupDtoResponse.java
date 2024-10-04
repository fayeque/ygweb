package com.yg.web.dto.responses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDtoResponse {
    private Long groupId;
    private String groupName;
    List<MemberDtoResponse> memberDtoResponse = new ArrayList<MemberDtoResponse>();
}
