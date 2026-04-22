package com.example.app.dto;

import java.time.Instant;

/**
 * DTO for structured error responses
 * Used by GlobalExceptionHandler to return consistent error format
 * 
 * @author Search API
 * @version 1.0
 * @see com.example.app.exception.GlobalExceptionHandler
 */
public class ErrorResponseDTO {
    
    private int status;
    private String error;
    private String message;
    private String timestamp;
    
    public ErrorResponseDTO() {
        this.timestamp = Instant.now().toString();
    }
    
    public ErrorResponseDTO(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now().toString();
    }
    
    // Getters and Setters
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
