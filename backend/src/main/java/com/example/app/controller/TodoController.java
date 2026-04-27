package com.example.app.controller;

import com.example.app.dto.PaginatedResponse;
import com.example.app.dto.TodoResponse;
import com.example.app.dto.TodoSearchRequest;
import com.example.app.model.Todo;
import com.example.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    /**
     * Search Todos endpoint with filtering, pagination, and sorting.
     * Supports optional query parameters:
     * - title (String, case-insensitive partial match)
     * - status (TodoStatus enum: PENDING, IN_PROGRESS, COMPLETED)
     * - dueDateFrom (LocalDate, ISO 8601: yyyy-MM-dd)
     * - dueDateTo (LocalDate, ISO 8601: yyyy-MM-dd)
     * - page (int, default 0)
     * - size (int, default 10, max 100)
     * - sort (String, format "field:direction", default "dueDate:asc")
     *
     * All query parameters are optional. Omitting a parameter means no filter/default value.
     *
     * @param searchRequest the search request DTO with validation
     * @return Paginated response with matching Todos
     */
    @GetMapping("/search")
    public ResponseEntity<PaginatedResponse<TodoResponse>> searchTodos(@Valid TodoSearchRequest searchRequest) {
        Page<Todo> todoPage = todoService.searchTodos(searchRequest);

        // Map Page<Todo> to PaginatedResponse<TodoResponse>
        PaginatedResponse<TodoResponse> response = PaginatedResponse.of(todoPage, TodoResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    // Existing CRUD endpoints below (unchanged)

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
        try {
            Todo updatedTodo = todoService.updateTodo(id, todoDetails);
            return ResponseEntity.ok(updatedTodo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
