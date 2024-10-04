package com.yg.web.dto.transactionDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String username;
    private Long groupId;
    private Integer year;
    private Integer amount;
    private String transactionPeriod;
}
