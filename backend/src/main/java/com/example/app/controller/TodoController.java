package com.example.app.controller;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.service.TodoSearchService;
import com.example.app.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<List<Todo>> getAll() {
        return ResponseEntity.ok(todoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getById(@PathVariable Long id) {
        return todoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        Todo created = todoService.save(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.findById(id)
                .map(existing -> {
                    todo.setId(id);
                    return ResponseEntity.ok(todoService.save(todo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        todoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search endpoint for Todo items with pagination, sorting, and filtering.
     * 
     * @param request Search criteria (title, status, dueDateFrom, dueDateTo, page, size, sort)
     * @return Paginated search results
     */
    @GetMapping("/search")
    public ResponseEntity<TodoSearchResponse> search(@Valid @ModelAttribute TodoSearchRequest request) {
        Page<Todo> page = todoSearchService.search(request);

        TodoSearchResponse response = new TodoSearchResponse(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );

        return ResponseEntity.ok(response);
    }
}
