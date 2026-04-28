package com.example.app.service;

import com.example.app.dto.TodoItemDto;
import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.TodoStatus;
import com.example.app.repository.TodoRepository;
import com.example.app.specification.TodoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TodoSearchService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("title", "status", "dueDate", "createdAt");

    private final TodoRepository todoRepository;

    public TodoSearchService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoSearchResponse search(TodoSearchRequest request) {
        Sort sort = buildSort(request.getSort());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Specification<com.example.app.model.Todo> spec = TodoSpecification.buildSpecification(
                request.getTitle(),
                request.getStatus(),
                request.getDueDateFrom(),
                request.getDueDateTo()
        );

        Page<com.example.app.model.Todo> page = todoRepository.findAll(spec, pageable);

        List<TodoItemDto> content = page.getContent().stream()
                .map(todo -> new TodoItemDto(
                        todo.getId(),
                        todo.getTitle(),
                        todo.getStatus(),
                        todo.getDueDate(),
                        todo.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return new TodoSearchResponse(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    private Sort buildSort(String sortParam) {
        if (sortParam == null || sortParam.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }

        String[] parts = sortParam.split(",");
        String field = parts[0].trim();
        String direction = parts.length > 1 ? parts[1].trim().toUpperCase() : "ASC";

        if (!ALLOWED_SORT_FIELDS.contains(field)) {
            throw new IllegalArgumentException(
                    "Invalid sort field. Allowed: " + String.join(", ", ALLOWED_SORT_FIELDS));
        }

        return Sort.by(Sort.Direction.valueOf(direction), field);
    }
}
