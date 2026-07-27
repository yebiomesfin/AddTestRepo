package com.example.app.controller;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.service.TodoSearchService;
import com.example.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoSearchService todoSearchService;

    public TodoController(TodoService todoService, TodoSearchService todoSearchService) {
        this.todoService = todoService;
        this.todoSearchService = todoSearchService;
    }

    // ── Existing CRUD Endpoints ──────────────────────────────────────────────

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo createTodo(@RequestBody Todo todo) {
        return todoService.createTodo(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.updateTodo(id, todo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

    // ── Search Endpoint (ADDAII-609, ADDAII-610, ADDAII-611, ADDAII-612, ADDAII-613, ADDAII-614) ──

    @GetMapping("/search")
    public ResponseEntity<?> searchTodos(@Valid TodoSearchRequest request) {

        // Validate status
        if (!request.hasValidStatus()) {
            return ResponseEntity.badRequest().body(buildError(
                    HttpStatus.BAD_REQUEST,
                    "Invalid status value: '" + request.getStatus() +
                    "'. Valid values are: " + TodoSearchRequest.getValidStatuses(),
                    "/api/todos/search"
            ));
        }

        // Validate sort field
        if (!request.hasValidSortField()) {
            return ResponseEntity.badRequest().body(buildError(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort field. Allowed fields: " + TodoSearchRequest.getAllowedSortFields(),
                    "/api/todos/search"
            ));
        }

        // Validate date range
        if (!request.isDateRangeValid()) {
            return ResponseEntity.badRequest().body(buildError(
                    HttpStatus.BAD_REQUEST,
                    "dueDateFrom must not be after dueDateTo",
                    "/api/todos/search"
            ));
        }

        TodoSearchResponse response = todoSearchService.search(request);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> buildError(HttpStatus status, String message, String path) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("path", path);
        return error;
    }
}
