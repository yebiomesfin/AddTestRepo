package com.example.app.controller;

import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.TodoStatus;
import com.example.app.service.TodoSearchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * REST controller for Todo operations.
 * Provides endpoints for searching, creating, updating, and deleting todos.
 */
@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    @Autowired
    private TodoSearchService todoSearchService;

    /**
     * Search endpoint for todos with filtering, pagination, and sorting.
     * 
     * @param title search keyword for title (case-insensitive, max 255 chars)
     * @param status filter by status (PENDING, IN_PROGRESS, COMPLETED)
     * @param dueDateFrom minimum due date (inclusive, ISO 8601 format: yyyy-MM-dd)
     * @param dueDateTo maximum due date (inclusive, ISO 8601 format: yyyy-MM-dd)
     * @param page page number (0-based, default 0, min 0)
     * @param size page size (default 10, min 1, max 100)
     * @param sort sort specification (default "createdAt,desc", format: "field,direction")
     * @return paginated search response with todos and metadata
     * 
     * Examples:
     * - GET /api/todos/search?title=grocery
     * - GET /api/todos/search?status=COMPLETED
     * - GET /api/todos/search?dueDateFrom=2026-01-01&dueDateTo=2026-12-31
     * - GET /api/todos/search?page=0&size=20&sort=dueDate,asc
     * - GET /api/todos/search?title=report&status=PENDING&dueDateFrom=2026-01-01&dueDateTo=2026-12-31&page=0&size=10&sort=dueDate,asc
     */
    @GetMapping("/search")
    public ResponseEntity<TodoSearchResponse> searchTodos(
            @RequestParam(required = false)
            @Size(max = 255, message = "title must not exceed 255 characters")
            String title,

            @RequestParam(required = false)
            TodoStatus status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueDateFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dueDateTo,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "page must be >= 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "size must be between 1 and 100")
            @Max(value = 100, message = "size must not exceed 100")
            int size,

            @RequestParam(required = false)
            String sort
    ) {
        TodoSearchResponse response = todoSearchService.searchTodos(
                title, status, dueDateFrom, dueDateTo, page, size, sort
        );
        return ResponseEntity.ok(response);
    }
}
