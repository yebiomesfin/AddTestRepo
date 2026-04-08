package com.example.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.exception.ResourceNotFoundException;
import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo todoDetails) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

        if (todoDetails.getTitle() != null) {
            todo.setTitle(todoDetails.getTitle());
        }
        if (todoDetails.getDescription() != null) {
            todo.setDescription(todoDetails.getDescription());
        }
        if (todoDetails.getCompleted() != null) {
            todo.setCompleted(todoDetails.getCompleted());
        }

        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
        todoRepository.delete(todo);
    }

    /**
     * Search todos by query text
     * Searches in both title and description fields (case-insensitive)
     * 
     * @param query the search text
     * @return list of todos matching the search criteria
     */
    public List<Todo> searchTodos(String query) {
        String searchQuery = query.toLowerCase().trim();
        
        return todoRepository.findAll().stream()
                .filter(todo -> {
                    String title = todo.getTitle() != null ? todo.getTitle().toLowerCase() : "";
                    String description = todo.getDescription() != null ? todo.getDescription().toLowerCase() : "";
                    return title.contains(searchQuery) || description.contains(searchQuery);
                })
                .collect(Collectors.toList());
    }
}