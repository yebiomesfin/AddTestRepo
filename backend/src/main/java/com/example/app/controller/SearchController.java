package com.example.app.controller;

import com.example.app.dto.SearchResponseDTO;
import com.example.app.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Search API
 * Endpoint: GET /api/v1/search
 * 
 * @author Search API
 * @version 1.0
 * @see SearchService
 */
@RestController
@RequestMapping("/api/v1")
public class SearchController {
    
    private final SearchService searchService;
    
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    
    /**
     * Search endpoint with keyword, category filter, pagination, and sorting
     * 
     * Query Parameters:
     * - q: Keyword search (optional, max 255 chars)
     * - category: Category filter (optional, max 100 chars)
     * - page: Page number (optional, default 0, min 0)
     * - size: Page size (optional, default 20, min 1, max 100)
     * - sortBy: Sort field (optional, default "createdDate", allowed: name, description, category, createdDate)
     * - sortDir: Sort direction (optional, default "desc", allowed: asc, desc)
     * 
     * @return SearchResponseDTO with paginated results and metadata
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> search(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String sortDir
    ) {
        SearchResponseDTO response = searchService.search(q, category, page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }
}
