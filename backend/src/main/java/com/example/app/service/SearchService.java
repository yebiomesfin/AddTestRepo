package com.example.app.service;

import com.example.app.dto.ItemDTO;
import com.example.app.dto.SearchResponseDTO;
import com.example.app.model.Item;
import com.example.app.repository.ItemRepository;
import com.example.app.specification.ItemSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for search API business logic
 * Handles dynamic filtering, pagination, sorting, and DTO mapping
 * 
 * @author Search API
 * @version 1.0
 * @see ItemRepository
 * @see ItemSpecification
 */
@Service
public class SearchService {
    
    private final ItemRepository itemRepository;
    
    // Allowed sort fields for validation
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList(
        "name", "description", "category", "createdDate"
    );
    
    // Constants for pagination and sorting defaults
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_SIZE = 100;
    private static final String DEFAULT_SORT_BY = "createdDate";
    private static final String DEFAULT_SORT_DIR = "desc";
    
    public SearchService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }
    
    /**
     * Search items with keyword, category filter, pagination, and sorting
     * 
     * @param keyword Optional keyword to search in name and description
     * @param category Optional category filter
     * @param page Page number (zero-indexed)
     * @param size Page size
     * @param sortBy Field to sort by
     * @param sortDir Sort direction (asc/desc)
     * @return SearchResponseDTO with paginated results and metadata
     * @throws IllegalArgumentException if validation fails
     */
    public SearchResponseDTO search(String keyword, String category, Integer page, Integer size, 
                                      String sortBy, String sortDir) {
        
        // Apply defaults
        int finalPage = (page != null) ? page : DEFAULT_PAGE;
        int finalSize = (size != null) ? size : DEFAULT_SIZE;
        String finalSortBy = (sortBy != null && !sortBy.trim().isEmpty()) ? sortBy : DEFAULT_SORT_BY;
        String finalSortDir = (sortDir != null && !sortDir.trim().isEmpty()) ? sortDir : DEFAULT_SORT_DIR;
        
        // Validate inputs
        validateSearchParams(finalPage, finalSize, finalSortBy, finalSortDir, keyword, category);
        
        // Build Specification
        Specification<Item> spec = ItemSpecification.buildFilter(keyword, category);
        
        // Build Sort
        Sort sort = Sort.by(
            finalSortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
            finalSortBy
        );
        
        // Build Pageable
        Pageable pageable = PageRequest.of(finalPage, finalSize, sort);
        
        // Execute query
        Page<Item> itemPage = itemRepository.findAll(spec, pageable);
        
        // Map to DTOs
        List<ItemDTO> itemDTOs = itemPage.getContent().stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        
        // Build response
        return new SearchResponseDTO(
            itemDTOs,
            itemPage.getNumber(),
            itemPage.getSize(),
            itemPage.getTotalElements(),
            itemPage.getTotalPages(),
            finalSortBy,
            finalSortDir
        );
    }
    
    /**
     * Validate search parameters
     * 
     * @throws IllegalArgumentException if validation fails
     */
    private void validateSearchParams(int page, int size, String sortBy, String sortDir, 
                                        String keyword, String category) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero");
        }
        
        if (size < 1 || size > MAX_SIZE) {
            throw new IllegalArgumentException("Page size must be between 1 and " + MAX_SIZE);
        }
        
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                "Invalid sortBy field. Allowed values: " + String.join(", ", ALLOWED_SORT_FIELDS)
            );
        }
        
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Invalid sortDir. Allowed values: asc, desc");
        }
        
        if (keyword != null && keyword.length() > 255) {
            throw new IllegalArgumentException("Keyword must not exceed 255 characters");
        }
        
        if (category != null && category.length() > 100) {
            throw new IllegalArgumentException("Category must not exceed 100 characters");
        }
    }
    
    /**
     * Convert Item entity to ItemDTO
     */
    private ItemDTO toDTO(Item item) {
        return new ItemDTO(
            item.getId(),
            item.getName(),
            item.getDescription(),
            item.getCategory(),
            item.getCreatedDate()
        );
    }
}
