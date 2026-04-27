package com.example.app.service;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;
import com.example.app.specification.TodoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList("title", "dueDate", "status");

    @Autowired
    private TodoRepository todoRepository;

    /**
     * Search Todos using dynamic filters, pagination, and sorting.
     *
     * @param searchRequest the search request with filters, pagination, and sort criteria
     * @return Page of Todos matching the search criteria
     * @throws IllegalArgumentException if sort field is invalid or dueDateFrom > dueDateTo
     */
    public Page<Todo> searchTodos(TodoSearchRequest searchRequest) {
        // Validate date range
        if (searchRequest.getDueDateFrom() != null && searchRequest.getDueDateTo() != null &&
                searchRequest.getDueDateFrom().isAfter(searchRequest.getDueDateTo())) {
            throw new IllegalArgumentException("dueDateFrom must be less than or equal to dueDateTo");
        }

        // Parse and validate sort parameter
        Sort sort = parseSortParameter(searchRequest.getSort());

        // Build pageable
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);

        // Build specification from request
        Specification<Todo> specification = TodoSpecification.buildSpecification(searchRequest);

        // Execute query
        return todoRepository.findAll(specification, pageable);
    }

    /**
     * Parse sort parameter in format "field:direction" (e.g., "title:asc", "dueDate:desc").
     * Validates field and direction.
     *
     * @param sortParam the sort parameter string
     * @return Sort object
     * @throws IllegalArgumentException if sort field or direction is invalid
     */
    private Sort parseSortParameter(String sortParam) {
        if (sortParam == null || sortParam.trim().isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "dueDate"); // default sort
        }

        String[] parts = sortParam.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid sort format. Expected 'field:direction', got: " + sortParam
            );
        }

        String field = parts[0].trim();
        String direction = parts[1].trim().toLowerCase();

        // Validate field
        if (!VALID_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Supported fields: " + String.join(", ", VALID_SORT_FIELDS)
            );
        }

        // Validate direction
        Sort.Direction sortDirection;
        if ("asc".equals(direction)) {
            sortDirection = Sort.Direction.ASC;
        } else if ("desc".equals(direction)) {
            sortDirection = Sort.Direction.DESC;
        } else {
            throw new IllegalArgumentException(
                    "Invalid sort direction. Supported directions: asc, desc"
            );
        }

        return Sort.by(sortDirection, field);
    }

    // Existing CRUD methods below (unchanged)

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
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setStatus(todoDetails.getStatus());
        todo.setDueDate(todoDetails.getDueDate());
        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}
