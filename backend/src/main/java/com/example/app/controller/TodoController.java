package com.example.app.controller;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.model.TodoStatus;
import com.example.app.service.TodoSearchService;
import com.example.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    // ─── Existing Endpoints (preserved) ────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Todo> create(@Valid @RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.create(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @Valid @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.update(id, todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ─── NEW: Todo Search Endpoint ──────────────────────────────────────────
    // ADDAII-597: Search todos by title (contains/substring match)
    // ADDAII-598: Filter todos by status
    // ADDAII-599: Filter todos by due date range
    // ADDAII-600: Paginate and sort search results
    // ADDAII-601: Handle validation errors for invalid search parameters
    // ADDAII-602: Return empty results gracefully when no todos match

    @GetMapping("/search")
    public ResponseEntity<TodoSearchResponse<Todo>> searchTodos(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TodoStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        TodoSearchRequest request = new TodoSearchRequest();
        request.setTitle(title);
        request.setStatus(status);
        request.setDueDateFrom(dueDateFrom);
        request.setDueDateTo(dueDateTo);
        request.setPage(page);
        request.setSize(size);
        request.setSort(sort);

        TodoSearchResponse<Todo> response = todoSearchService.search(request);
        return ResponseEntity.ok(response);
    }
}