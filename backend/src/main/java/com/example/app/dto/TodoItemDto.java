package com.example.app.dto;

import com.example.app.model.TodoStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TodoItemDto {

    private Long id;
    private String title;
    private TodoStatus status;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    public TodoItemDto(Long id, String title, TodoStatus status,
                       LocalDate dueDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public TodoStatus getStatus() { return status; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
