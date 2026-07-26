package com.example.app.dto;

import com.example.app.model.TodoStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class TodoSearchRequest {

    private String title;
    private TodoStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDateTo;

    private int page = 0;
    private int size = 10;
    private String sort = "createdAt,desc";

    public TodoSearchRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public TodoStatus getStatus() { return status; }
    public void setStatus(TodoStatus status) { this.status = status; }

    public LocalDate getDueDateFrom() { return dueDateFrom; }
    public void setDueDateFrom(LocalDate dueDateFrom) { this.dueDateFrom = dueDateFrom; }

    public LocalDate getDueDateTo() { return dueDateTo; }
    public void setDueDateTo(LocalDate dueDateTo) { this.dueDateTo = dueDateTo; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
}