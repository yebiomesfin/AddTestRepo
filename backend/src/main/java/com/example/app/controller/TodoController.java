package com.example.app.controller;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.service.TodoSearchService;
import com.example.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoSearchService todoSearchService;

    public TodoController(TodoService todoService, TodoSearchService todoSearchService) {
        this.todoService = todoService;
        this.todoSearchService = todoSearchService;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        return todoService.updateTodo(id, todoDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        return todoService.deleteTodo(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<TodoSearchResponse> searchTodos(@Valid @ModelAttribute TodoSearchRequest request) {
        // Validate at least one search parameter is provided (ADDAII-538)
        if (!request.hasAtLeastOneSearchParam()) {
            throw new IllegalArgumentException("At least one search parameter must be provided");
        }

        // Validate date range if both are provided (ADDAII-538)
        if (request.getDueDateFrom() != null && request.getDueDateTo() != null
                && request.getDueDateFrom().isAfter(request.getDueDateTo())) {
            throw new IllegalArgumentException("dueDateFrom must be <= dueDateTo");
        }

        TodoSearchResponse response = todoSearchService.search(request);
        return ResponseEntity.ok(response);
    }
}
