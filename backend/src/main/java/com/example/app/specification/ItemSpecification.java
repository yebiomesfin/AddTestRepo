package com.example.app.specification;

import com.example.app.model.Item;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specification builder for dynamic Item filtering
 * Supports keyword search on name/description and exact category match
 * 
 * @author Search API
 * @version 1.0
 * @see Item
 * @see com.example.app.service.SearchService
 */
public class ItemSpecification {
    
    /**
     * Build dynamic filter specification based on search criteria
     * 
     * @param keyword Optional keyword to search in name and description (case-insensitive, partial match)
     * @param category Optional category to filter by (exact match)
     * @return Specification for filtering Item entities
     */
    public static Specification<Item> buildFilter(String keyword, String category) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Keyword search: case-insensitive LIKE on name OR description
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), 
                    likePattern
                );
                Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), 
                    likePattern
                );
                predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
            }
            
            // Category filter: exact match
            if (category != null && !category.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category.trim()));
            }
            
            // Combine all predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
