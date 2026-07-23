package com.example.app.controller;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.service.TodoSearchService;
import com.example.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    @Autowired
    private TodoService todoService;
    
    @Autowired
    private TodoSearchService todoSearchService;

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
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.createTodo(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.updateTodo(id, todo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Search/filter Todos with pagination and sorting.
     * 
     * ADDAII-588: Title search (case-insensitive, partial match)
     * ADDAII-589: Filter by status and due date range
     * ADDAII-590: Pagination and sorting
     * 
     * @param request Search parameters (title, status, dueDateFrom, dueDateTo, page, size, sort)
     * @return Paginated search results
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchTodos(@Valid @ModelAttribute TodoSearchRequest request) {
        // Validate date range
        if (!request.isValidDateRange()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("timestamp", LocalDateTime.now());
            errorResponse.put("status", 400);
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Invalid date range: dueDateFrom must be before or equal to dueDateTo");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        TodoSearchResponse response = todoSearchService.searchTodos(request);
        return ResponseEntity.ok(response);
    }
}