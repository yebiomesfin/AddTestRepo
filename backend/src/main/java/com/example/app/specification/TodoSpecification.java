package com.example.app.specification;

import com.example.app.dto.TodoSearchRequest;
import com.example.app.model.Todo;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification builder for dynamic Todo search queries.
 * Constructs predicates based on non-null search criteria.
 */
public class TodoSpecification {

    /**
     * Build a JPA Specification from TodoSearchRequest.
     * Each predicate is only added if its corresponding request field is non-null.
     *
     * @param request the search request with filter criteria
     * @return Specification for dynamic query execution
     */
    public static Specification<Todo> buildSpecification(TodoSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Title: case-insensitive partial match (LIKE %title%)
            if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
                String titlePattern = "%" + request.getTitle().toLowerCase() + "%";
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                titlePattern
                        )
                );
            }

            // Status: exact enum match
            if (request.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("status"), request.getStatus())
                );
            }

            // DueDateFrom: dueDate >= dueDateFrom (inclusive lower bound)
            if (request.getDueDateFrom() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), request.getDueDateFrom())
                );
            }

            // DueDateTo: dueDate <= dueDateTo (inclusive upper bound)
            if (request.getDueDateTo() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), request.getDueDateTo())
                );
            }

            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
