package com.yg.web.service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yg.web.dto.responses.SuccessResponseDto;


@Service
public class BuildResponseUtils {
	
	public <T> ResponseEntity<SuccessResponseDto<T>> buildSuccessResponseDto(String message, T data) {
		SuccessResponseDto<T> successResponseDto =  new SuccessResponseDto<T>(
   			 HttpStatus.OK.value(),
             "",
              message,
              "",
              data);
		
  	 	return ResponseEntity.ok(successResponseDto);

	}

}
