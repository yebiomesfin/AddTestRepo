package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.app.exception.ResourceNotFoundException;
import com.example.app.model.Todo;
import com.example.app.service.TodoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        Todo updatedTodo = todoService.updateTodo(id, todoDetails);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Search todos by text query
     * Searches in both title and description fields
     * 
     * @param query the search text
     * @return list of todos matching the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<Todo>> searchTodos(@RequestParam(required = false) String query) {
        List<Todo> searchResults;
        
        if (query == null || query.trim().isEmpty()) {
            // If no query provided, return all todos
            searchResults = todoService.getAllTodos();
        } else {
            // Perform search with the provided query
            searchResults = todoService.searchTodos(query);
        }
        
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }
}