package com.example.app.dto;

import com.example.app.model.TodoStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

/**
 * Request DTO for Todo search API with validation rules.
 * All fields are optional; omitting a field means no filter is applied for that criterion.
 */
public class TodoSearchRequest {

    /**
     * Title search term (partial, case-insensitive match)
     */
    private String title;

    /**
     * Todo status filter (PENDING, IN_PROGRESS, COMPLETED)
     */
    private TodoStatus status;

    /**
     * Due date range lower bound (inclusive)
     */
    private LocalDate dueDateFrom;

    /**
     * Due date range upper bound (inclusive)
     */
    private LocalDate dueDateTo;

    /**
     * Page index (0-based), default 0
     */
    @Min(value = 0, message = "page must be greater than or equal to 0")
    private int page = 0;

    /**
     * Page size (number of results per page), default 10, max 100
     */
    @Min(value = 1, message = "size must be between 1 and 100")
    @Max(value = 100, message = "size must be between 1 and 100")
    private int size = 10;

    /**
     * Sort specification in format "field:direction" (e.g., "title:asc", "dueDate:desc")
     * Supported fields: title, dueDate, status
     * Supported directions: asc, desc
     * Default: "dueDate:asc"
     */
    private String sort = "dueDate:asc";

    // Constructors
    public TodoSearchRequest() {
    }

    public TodoSearchRequest(String title, TodoStatus status, LocalDate dueDateFrom, LocalDate dueDateTo,
                             int page, int size, String sort) {
        this.title = title;
        this.status = status;
        this.dueDateFrom = dueDateFrom;
        this.dueDateTo = dueDateTo;
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
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
}
