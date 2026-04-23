package com.example.app.dto;

public class ErrorResponse {
    private int status;
    private String error;
    private String code;
    private String message;
    private String timestamp;
    private String path;
    public ErrorResponse() {}
    public ErrorResponse(int status, String error, String code, String message, String timestamp, String path) {
        this.status = status; this.error = error; this.code = code; this.message = message; this.timestamp = timestamp; this.path = path;
    }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}