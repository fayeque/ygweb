package com.yg.web.dto.responses;

import java.time.LocalDateTime;
import java.util.Date;

import com.yg.web.enums.CollectionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDtoResponse {
    private Long id;
    private CollectionType transactionType;
    private String transactionPeriod;
    private int year;
    private Integer amount;
    private LocalDateTime transactionDate;
}
