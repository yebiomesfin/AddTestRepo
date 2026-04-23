package com.example.app.exception;

import com.example.app.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        return ResponseEntity.status(400).body(new ErrorResponse(400, "Bad Request", "MISSING_QUERY", "Search query parameter 'q' is required", Instant.now().toString(), request.getRequestURI()));
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest request) {
        String raw = ex.getConstraintViolations().iterator().next().getMessage();
        String code = "INVALID_INPUT"; String msg = raw;
        if (raw != null && raw.contains(":")) { String[] p = raw.split(":", 2); code = p[0]; msg = p[1]; }
        return ResponseEntity.status(400).body(new ErrorResponse(400, "Bad Request", code, msg, Instant.now().toString(), request.getRequestURI()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegal(IllegalArgumentException ex, HttpServletRequest request) {
        String raw = ex.getMessage(); String code = "INVALID_INPUT"; String msg = raw;
        if (raw != null && raw.contains(":")) { String[] p = raw.split(":", 2); code = p[0]; msg = p[1]; }
        return ResponseEntity.status(400).body(new ErrorResponse(400, "Bad Request", code, msg, Instant.now().toString(), request.getRequestURI()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return ResponseEntity.status(500).body(new ErrorResponse(500, "Internal Server Error", "INTERNAL_ERROR", "An unexpected error occurred", Instant.now().toString(), request.getRequestURI()));
    }
}