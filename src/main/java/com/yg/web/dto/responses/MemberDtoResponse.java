package com.yg.web.dto.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDtoResponse {
    private Long id;
    private String username;
    private String name;
    private List<TransactionDtoResponse> transactionDtoResponse;
}
