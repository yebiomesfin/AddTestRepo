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
import java.util.List;

@Service
public class TodoSearchService {

    private final TodoRepository todoRepository;

    public TodoSearchService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoSearchResponse search(TodoSearchRequest request) {
        Specification<Todo> spec = buildSpecification(request);
        Pageable pageable = buildPageable(request);

        Page<Todo> page = todoRepository.findAll(spec, pageable);

        return new TodoSearchResponse(
                page.getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }

    private Specification<Todo> buildSpecification(TodoSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + request.getTitle().toLowerCase() + "%"
                ));
            }

            if (request.getStatus() != null && !request.getStatus().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus().toUpperCase()));
            }

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
        String sortParam = request.getSort() != null ? request.getSort() : "dueDate,asc";
        String[] sortParts = sortParam.split(",");
        String sortField = sortParts[0].trim();
        Sort.Direction direction = (sortParts.length > 1 && sortParts[1].trim().equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(request.getPage(), request.getSize(), Sort.by(direction, sortField));
    }
}
