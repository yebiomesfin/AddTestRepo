package com.example.app.dto;

import com.example.app.model.Todo;
import com.example.app.model.TodoStatus;

import java.time.LocalDate;

/**
 * Response DTO for Todo entity.
 * Maps the Todo entity to the API response shape.
 */
public class TodoResponse {

    private Long id;
    private String title;
    private String description;
    private TodoStatus status;
    private LocalDate dueDate;

    // Constructors
    public TodoResponse() {
    }

    public TodoResponse(Long id, String title, String description, TodoStatus status, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    /**
     * Factory method to create TodoResponse from Todo entity
     * @param todo the Todo entity
     * @return TodoResponse instance
     */
    public static TodoResponse fromEntity(Todo todo) {
        if (todo == null) {
            return null;
        }
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.getStatus(),
                todo.getDueDate()
        );
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
