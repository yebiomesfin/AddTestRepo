package com.example.app.service;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service for Todo search operations using JPA Specifications.
 * Implements dynamic filtering and pagination for search endpoint.
 * Relates to Jira tickets: ADDAII-603, ADDAII-604, ADDAII-605, ADDAII-606
 */
@Service
public class TodoSearchService {

    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("id", "title", "status", "dueDate");
    private static final List<String> VALID_STATUS_VALUES = Arrays.asList("PENDING", "IN_PROGRESS", "COMPLETED");

    private final TodoRepository todoRepository;

    public TodoSearchService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Search todos with dynamic filters and pagination.
     */
    public TodoSearchResponse search(TodoSearchRequest request) {
        // Validate date range
        if (request.getDueDateFrom() != null && request.getDueDateTo() != null) {
            if (request.getDueDateFrom().isAfter(request.getDueDateTo())) {
                throw new IllegalArgumentException("dueDateFrom must not be after dueDateTo");
            }
        }

        // Validate status
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            String statusUpper = request.getStatus().toUpperCase();
            if (!VALID_STATUS_VALUES.contains(statusUpper)) {
                throw new IllegalArgumentException(
                        "status must be one of: PENDING, IN_PROGRESS, COMPLETED");
            }
        }

        // Build dynamic specification
        Specification<Todo> spec = buildSpecification(request);

        // Build pageable with validated sort
        Pageable pageable = buildPageable(request);

        // Execute query
        Page<Todo> page = todoRepository.findAll(spec, pageable);

        // Map to response DTO
        return TodoSearchResponse.fromPage(page);
    }

    /**
     * Build JPA Specification from search request.
     */
    private Specification<Todo> buildSpecification(TodoSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Title filter (case-insensitive contains)
            if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + request.getTitle().toLowerCase() + "%"
                ));
            }

            // Status filter (case-insensitive exact match)
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.upper(root.get("status")),
                        request.getStatus().toUpperCase()
                ));
            }

            // Due date from filter
            if (request.getDueDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("dueDate"),
                        request.getDueDateFrom()
                ));
            }

            // Due date to filter
            if (request.getDueDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("dueDate"),
                        request.getDueDateTo()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Build Pageable with validated sort field.
     */
    private Pageable buildPageable(TodoSearchRequest request) {
        Sort sort = parseSort(request.getSort());
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    /**
     * Parse and validate sort parameter.
     * Format: "field,direction" e.g. "title,desc"
     */
    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return Sort.by("dueDate").ascending();
        }

        String[] parts = sortParam.split(",");
        String field = parts[0].trim();
        String direction = parts.length > 1 ? parts[1].trim() : "asc";

        // Validate field against whitelist
        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed fields: " + String.join(", ", ALLOWED_SORT_FIELDS));
        }

        if ("desc".equalsIgnoreCase(direction)) {
            return Sort.by(field).descending();
        } else {
            return Sort.by(field).ascending();
        }
    }
}