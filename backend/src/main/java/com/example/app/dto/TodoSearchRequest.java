package com.example.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class TodoSearchRequest {
    
    private String title;
    
    @Pattern(regexp = "OPEN|IN_PROGRESS|DONE", message = "Invalid status value. Allowed values: OPEN, IN_PROGRESS, DONE")
    private String status;
    
    private LocalDate dueDateFrom;
    
    private LocalDate dueDateTo;
    
    @Min(value = 0, message = "Page number must be >= 0")
    private int page = 0;
    
    @Min(value = 1, message = "Page size must be >= 1")
    @Max(value = 100, message = "Page size must be <= 100")
    private int size = 10;
    
    @Pattern(regexp = "(id|title|status|dueDate|createdAt),(asc|desc)", message = "Invalid sort format. Use: field,direction (e.g., dueDate,asc)")
    private String sort = "dueDate,asc";
    
    // Getters and Setters
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDate getDueDateFrom() {
        return dueDateFrom;
    }
    
    public void setDueDateFrom(LocalDate dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }
    
    public LocalDate getDueDateTo() {
        return dueDateTo;
    }
    
    public void setDueDateTo(LocalDate dueDateTo) {
        this.dueDateTo = dueDateTo;
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public String getSort() {
        return sort;
    }
    
    public void setSort(String sort) {
        this.sort = sort;
    }
    
    // Custom validation method
    public boolean isValidDateRange() {
        if (dueDateFrom != null && dueDateTo != null) {
            return !dueDateFrom.isAfter(dueDateTo);
        }
        return true;
    }
}