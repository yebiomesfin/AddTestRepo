package com.example.app.service;

import com.example.app.dto.TodoDTO;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.model.TodoStatus;
import com.example.app.repository.TodoRepository;
import com.example.app.specification.TodoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for handling Todo search operations.
 * Validates input, builds specifications, and maps results to DTOs.
 */
@Service
public class TodoSearchService {

    private static final Set<String> VALID_SORT_FIELDS = Set.of("title", "status", "dueDate", "createdAt");
    private static final String DEFAULT_SORT = "createdAt,desc";

    @Autowired
    private TodoRepository todoRepository;

    /**
     * Searches for todos based on the provided filters and pagination parameters.
     * 
     * @param title search keyword for title (case-insensitive)
     * @param status filter by status
     * @param dueDateFrom minimum due date (inclusive)
     * @param dueDateTo maximum due date (inclusive)
     * @param page page number (0-based)
     * @param size page size (1-100)
     * @param sort sort specification (e.g., "dueDate,asc")
     * @return paginated search response
     * @throws IllegalArgumentException if validation fails
     */
    public TodoSearchResponse searchTodos(String title, TodoStatus status, 
                                         LocalDate dueDateFrom, LocalDate dueDateTo,
                                         int page, int size, String sort) {
        // Validate date range
        validateDateRange(dueDateFrom, dueDateTo);

        // Build Sort object
        Sort sortObj = buildSort(sort);

        // Build Pageable
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // Build Specification
        Specification<Todo> spec = TodoSpecification.buildSpecification(title, status, dueDateFrom, dueDateTo);

        // Execute query
        Page<Todo> todoPage = todoRepository.findAll(spec, pageable);

        // Map to response
        return mapToResponse(todoPage);
    }

    /**
     * Validates that dueDateFrom is not after dueDateTo.
     */
    private void validateDateRange(LocalDate dueDateFrom, LocalDate dueDateTo) {
        if (dueDateFrom != null && dueDateTo != null && dueDateFrom.isAfter(dueDateTo)) {
            throw new IllegalArgumentException("dueDateFrom must not be after dueDateTo");
        }
    }

    /**
     * Builds a Sort object from the sort parameter string.
     * Validates that the sort field is in the whitelist.
     * 
     * @param sort sort specification (e.g., "dueDate,asc" or "title,desc")
     * @return Sort object
     * @throws IllegalArgumentException if sort field is invalid
     */
    private Sort buildSort(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            sort = DEFAULT_SORT;
        }

        String[] parts = sort.split(",");
        String field = parts[0].trim();
        String direction = parts.length > 1 ? parts[1].trim() : "asc";

        // Validate sort field
        if (!VALID_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException(
                String.format("Invalid sort field '%s'. Valid fields are: %s", 
                             field, String.join(", ", VALID_SORT_FIELDS))
            );
        }

        Sort.Direction dir = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    /**
     * Maps a Page of Todo entities to TodoSearchResponse DTO.
     */
    private TodoSearchResponse mapToResponse(Page<Todo> todoPage) {
        List<TodoDTO> content = todoPage.getContent().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());

        return new TodoSearchResponse(
            content,
            todoPage.getNumber(),
            todoPage.getSize(),
            todoPage.getTotalElements(),
            todoPage.getTotalPages(),
            todoPage.isLast()
        );
    }

    /**
     * Maps a Todo entity to TodoDTO.
     */
    private TodoDTO mapToDTO(Todo todo) {
        return new TodoDTO(
            todo.getId(),
            todo.getTitle(),
            todo.getDescription(),
            todo.getStatus(),
            todo.getDueDate(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
}
