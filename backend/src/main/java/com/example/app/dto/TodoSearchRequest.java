package com.example.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

/**
 * Request DTO for Todo search endpoint.
 * All filter params are optional; only non-null values are applied.
 * Relates to Jira tickets: ADDAII-603, ADDAII-604, ADDAII-605, ADDAII-606, ADDAII-607
 */
public class TodoSearchRequest {

    // Filter parameters
    private String title;
    private String status;
    private LocalDate dueDateFrom;
    private LocalDate dueDateTo;

    // Pagination parameters
    @Min(value = 0, message = "page must be >= 0")
    private Integer page = 0;

    @Min(value = 1, message = "size must be between 1 and 100")
    @Max(value = 100, message = "size must be between 1 and 100")
    private Integer size = 10;

    private String sort = "dueDate,asc"; // Default sort

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}