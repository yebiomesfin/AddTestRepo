package com.example.app.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @GetMapping
    public List<String> getAllTodos() {
        List<String> todos = new ArrayList<>();
        todos.add("Learn Spring Boot");
        todos.add("Build REST API");
        return todos;
    }

    @GetMapping("/{id}")
    public String getTodoById(@PathVariable Long id) {
        return "Todo with ID: " + id;
    }

    @PostMapping
    public String createTodo(@RequestBody String todo) {
        return "Created todo: " + todo;
    }

    @PutMapping("/{id}")
    public String updateTodo(@PathVariable Long id, @RequestBody String todo) {
        return "Updated todo " + id + " with: " + todo;
    }

    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable Long id) {
        return "Deleted todo with ID: " + id;
    }

    // NEW API ENDPOINT: Search todos by status
    @GetMapping("/search")
    public List<String> searchByStatus(@RequestParam(required = false) String status) {
        List<String> todos = new ArrayList<>();
        if ("completed".equalsIgnoreCase(status)) {
            todos.add("Completed: Learn Spring Boot");
            todos.add("Completed: Build REST API");
        } else if ("pending".equalsIgnoreCase(status)) {
            todos.add("Pending: Write documentation");
            todos.add("Pending: Add unit tests");
        } else {
            todos.add("All todos returned");
        }
        return todos;
    }
}
