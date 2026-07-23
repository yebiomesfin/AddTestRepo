package com.example.app.service;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TodoSearchService {

    private final TodoRepository todoRepository;

    private static final List<String> VALID_STATUSES = Arrays.asList("OPEN", "IN_PROGRESS", "COMPLETED");
    private static final List<String> VALID_SORT_FIELDS = Arrays.asList("id", "title", "dueDate", "createdAt");

    public TodoSearchService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Page<Todo> search(TodoSearchRequest request) {
        // Cross-field validation: dueDateFrom <= dueDateTo
        if (request.getDueDateFrom() != null && request.getDueDateTo() != null) {
            if (request.getDueDateFrom().isAfter(request.getDueDateTo())) {
                throw new IllegalArgumentException("dueDateFrom must not be after dueDateTo");
            }
        }

        // Status enum validation
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            if (!VALID_STATUSES.contains(request.getStatus())) {
                throw new IllegalArgumentException("status must be one of " + VALID_STATUSES);
            }
        }

        // Build specification (dynamic WHERE clause)
        Specification<Todo> spec = buildSpecification(request);

        // Build pageable (pagination + sorting)
        Pageable pageable = buildPageable(request);

        // Execute query
        return todoRepository.findAll(spec, pageable);
    }

    private Specification<Todo> buildSpecification(TodoSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Title filter (case-insensitive LIKE)
            if (request.getTitle() != null && !request.getTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + request.getTitle().toLowerCase() + "%"
                ));
            }

            // Status filter (exact match)
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            // Due date range filter
            if (request.getDueDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), request.getDueDateFrom()));
            }
            if (request.getDueDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), request.getDueDateTo()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Pageable buildPageable(TodoSearchRequest request) {
        // Parse sort parameter: "field,direction"
        String sortField = "id";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (request.getSort() != null && !request.getSort().isEmpty()) {
            String[] sortParts = request.getSort().split(",");
            if (sortParts.length >= 1) {
                String requestedField = sortParts[0].trim();
                if (!VALID_SORT_FIELDS.contains(requestedField)) {
                    throw new IllegalArgumentException("sort field must be one of " + VALID_SORT_FIELDS);
                }
                sortField = requestedField;
            }
            if (sortParts.length >= 2) {
                String direction = sortParts[1].trim().toUpperCase();
                if ("DESC".equals(direction)) {
                    sortDirection = Sort.Direction.DESC;
                } else if (!"ASC".equals(direction)) {
                    throw new IllegalArgumentException("sort direction must be 'asc' or 'desc'");
                }
            }
        }

        Sort sort = Sort.by(sortDirection, sortField);
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }
}
