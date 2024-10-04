package com.yg.web.dto.responses;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public abstract class ApiResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
	

}