package com.example.app.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Structured error response body for API validation and runtime errors.
 */
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<String> fieldErrors;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
        this.fieldErrors = new ArrayList<>();
    }

    public ApiError(int status, String error, String message) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ApiError(int status, String error, String message, List<String> fieldErrors) {
        this(status, error, message);
        this.fieldErrors = fieldErrors;
    }

    // Getters and Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

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

    public List<String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public void addFieldError(String fieldError) {
        this.fieldErrors.add(fieldError);
    }
}
