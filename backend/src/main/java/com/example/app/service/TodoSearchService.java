package com.example.app.service;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.model.TodoStatus;
import com.example.app.repository.TodoRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Set;

@Service
public class TodoSearchService {

    private static final Logger log = LoggerFactory.getLogger(TodoSearchService.class);

    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of("id", "title", "status", "dueDate", "createdAt");

    private final TodoRepository todoRepository;

    public TodoSearchService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoSearchResponse<Todo> search(TodoSearchRequest request) {
        validateRequest(request);

        Pageable pageable = buildPageable(request);
        Specification<Todo> spec = buildSpecification(request);

        Page<Todo> result = todoRepository.findAll(spec, pageable);

        return new TodoSearchResponse<>(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast()
        );
    }

    private void validateRequest(TodoSearchRequest request) {
        if (request.getPage() < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (request.getSize() < 1 || request.getSize() > 100) {
            throw new IllegalArgumentException("size must be between 1 and 100");
        }
        if (request.getDueDateFrom() != null && request.getDueDateTo() != null) {
            if (request.getDueDateFrom().isAfter(request.getDueDateTo())) {
                throw new IllegalArgumentException("dueDateFrom must not be after dueDateTo");
            }
        }
        if (request.getSort() != null && !request.getSort().isBlank()) {
            String sortField = request.getSort().split(",")[0].trim();
            if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
                throw new IllegalArgumentException(
                        "Invalid sort field '" + sortField + "'. Allowed fields: " + ALLOWED_SORT_FIELDS);
            }
        }
    }

    private Pageable buildPageable(TodoSearchRequest request) {
        String sortParam = request.getSort() != null ? request.getSort() : "createdAt,desc";
        String[] parts = sortParam.split(",");
        String field = parts[0].trim();
        Sort.Direction direction = (parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, field));
    }

    private Specification<Todo> buildSpecification(TodoSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("title")),
                        "%" + request.getTitle().toLowerCase() + "%"
                ));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            if (request.getDueDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), request.getDueDateFrom()));
            }

            if (request.getDueDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), request.getDueDateTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}