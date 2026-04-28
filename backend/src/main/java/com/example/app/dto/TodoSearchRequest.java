package com.example.app.dto;

import com.example.app.model.TodoStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class TodoSearchRequest {

    private String title;
    private TodoStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDateTo;

    @Min(value = 0, message = "page must be >= 0")
    private Integer page = 0;

    @Min(value = 1, message = "size must be between 1 and 100")
    @Max(value = 100, message = "size must be between 1 and 100")
    private Integer size = 10;

    private String sort = "createdAt,desc";

    // Getters and Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TodoStatus getStatus() { return status; }
    public void setStatus(TodoStatus status) { this.status = status; }

    public LocalDate getDueDateFrom() { return dueDateFrom; }
    public void setDueDateFrom(LocalDate dueDateFrom) { this.dueDateFrom = dueDateFrom; }

    public LocalDate getDueDateTo() { return dueDateTo; }
    public void setDueDateTo(LocalDate dueDateTo) { this.dueDateTo = dueDateTo; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    public boolean hasAtLeastOneSearchParam() {
        return title != null || status != null || dueDateFrom != null || dueDateTo != null;
    }
}
