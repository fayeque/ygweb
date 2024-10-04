package com.yg.web.dto.dbToObjMapDto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberTransactionDTO {
    private Long memberId;
    private String username;
    private String name;
    private Long transactionId;
    private Integer amount;
    private Integer year;
    private String transactionType;
    private LocalDateTime transactionDate;



}
