package com.example.app.service;

import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Long id, Todo updated) {
        Todo existing = findById(id);
        existing.setTitle(updated.getTitle());
        existing.setCompleted(updated.isCompleted());
        return todoRepository.save(existing);
    }

    public void delete(Long id) {
        Todo existing = findById(id);
        todoRepository.delete(existing);
    }
}
