package com.yg.web.dto.responses;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SuccessResponseDto<T> extends ApiResponse{

	public T data;

    public SuccessResponseDto(int status, String error, String message, String path,T data) {
    	super(status, error, message, path);
    	this.data=data;
    }
    

}
