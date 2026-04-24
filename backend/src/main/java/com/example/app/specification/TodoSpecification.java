package com.example.app.specification;

import com.example.app.model.Todo;
import com.example.app.model.TodoStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

/**
 * JPA Specification builder for Todo entity.
 * Provides static factory methods to create predicates for filtering todos.
 */
public class TodoSpecification {

    /**
     * Creates a specification for case-insensitive title search using LIKE.
     * 
     * @param title the keyword to search for in todo titles
     * @return Specification for title contains search
     */
    public static Specification<Todo> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")),
                "%" + title.toLowerCase() + "%"
            );
        };
    }

    /**
     * Creates a specification for exact status match.
     * 
     * @param status the status to filter by
     * @return Specification for status equals
     */
    public static Specification<Todo> hasStatus(TodoStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Creates a specification for due date greater than or equal to.
     * 
     * @param from the minimum due date (inclusive)
     * @return Specification for dueDate >= from
     */
    public static Specification<Todo> dueDateFrom(LocalDate from) {
        return (root, query, criteriaBuilder) -> {
            if (from == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), from);
        };
    }

    /**
     * Creates a specification for due date less than or equal to.
     * 
     * @param to the maximum due date (inclusive)
     * @return Specification for dueDate <= to
     */
    public static Specification<Todo> dueDateTo(LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (to == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), to);
        };
    }

    /**
     * Composes all filter specifications with AND logic.
     * 
     * @param title search keyword for title
     * @param status status filter
     * @param dueDateFrom minimum due date
     * @param dueDateTo maximum due date
     * @return combined Specification with all filters
     */
    public static Specification<Todo> buildSpecification(String title, TodoStatus status,
                                                         LocalDate dueDateFrom, LocalDate dueDateTo) {
        return Specification.where(titleContains(title))
                           .and(hasStatus(status))
                           .and(dueDateFrom(dueDateFrom))
                           .and(dueDateTo(dueDateTo));
    }
}
