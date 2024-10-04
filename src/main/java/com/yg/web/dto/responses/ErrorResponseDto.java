package com.yg.web.dto.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class ErrorResponseDto extends ApiResponse {


    public ErrorResponseDto(int status, String error, String message, String path) {
    	super(status, error, message, path);
    }
}
 