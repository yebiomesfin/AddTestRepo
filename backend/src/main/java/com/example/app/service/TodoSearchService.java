package com.example.app.service;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.dto.TodoSearchResponse;
import com.example.app.model.Todo;
import com.example.app.repository.TodoRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private TodoRepository todoRepository;
    
    public TodoSearchResponse searchTodos(TodoSearchRequest request) {
        // Build dynamic specification
        Specification<Todo> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Title filter (case-insensitive partial match)
            if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), 
                    "%" + request.getTitle().toLowerCase() + "%"
                ));
            }
            
            // Status filter (exact match)
            if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            
            // Due date range filter (inclusive)
            if (request.getDueDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), request.getDueDateFrom()));
            }
            
            if (request.getDueDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), request.getDueDateTo()));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // Parse sort parameter
        Sort sort = parseSort(request.getSort());
        
        // Build pageable
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        
        // Execute query
        Page<Todo> page = todoRepository.findAll(spec, pageable);
        
        // Map to response DTO
        return new TodoSearchResponse(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }
    
    private Sort parseSort(String sortParam) {
        if (sortParam == null || sortParam.trim().isEmpty()) {
            return Sort.by(Sort.Direction.ASC, "dueDate");
        }
        
        String[] parts = sortParam.split(",");
        if (parts.length != 2) {
            return Sort.by(Sort.Direction.ASC, "dueDate");
        }
        
        String field = parts[0].trim();
        String direction = parts[1].trim().toUpperCase();
        
        Sort.Direction sortDirection = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        return Sort.by(sortDirection, field);
    }
}