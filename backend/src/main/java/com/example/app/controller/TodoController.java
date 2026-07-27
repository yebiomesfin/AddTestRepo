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

/**
 * REST Controller for Todo operations.
 * Includes search endpoint for Jira tickets: ADDAII-603 through ADDAII-608.
 */
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    private final TodoService todoService;
    private final TodoSearchService todoSearchService;

    public TodoController(TodoService todoService, TodoSearchService todoSearchService) {
        this.todoService = todoService;
        this.todoSearchService = todoSearchService;
    }

    /**
     * Search endpoint with filtering, pagination, and sorting.
     * GET /api/todos/search?title=...&status=...&dueDateFrom=...&dueDateTo=...&page=0&size=10&sort=dueDate,asc
     * 
     * Relates to Jira: ADDAII-603, ADDAII-604, ADDAII-605, ADDAII-606, ADDAII-607, ADDAII-608
     */
    @GetMapping("/search")
    public ResponseEntity<TodoSearchResponse> search(@Valid @ModelAttribute TodoSearchRequest request) {
        TodoSearchResponse response = todoSearchService.search(request);
        return ResponseEntity.ok(response);
    }

    // Existing CRUD endpoints (preserved)
    
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.updateTodo(id, todo);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }
}