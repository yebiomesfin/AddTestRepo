package com.example.app.controller;

import com.example.app.dto.ItemDto;
import com.example.app.dto.SearchResponse;
import com.example.app.model.Item;
import com.example.app.service.SearchService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@Validated
public class SearchController {
    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("name", "description", "category", "createdDate");
    private final SearchService searchService;
    public SearchController(SearchService searchService) { this.searchService = searchService; }

    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam @NotBlank(message = "MISSING_QUERY:Search query parameter 'q' is required") @Size(min = 2, message = "INVALID_QUERY:Search query must be at least 2 characters") @Size(max = 200, message = "INVALID_QUERY:Search query must not exceed 200 characters") String q,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        if (size > 100) throw new IllegalArgumentException("INVALID_PAGINATION:Page size must not exceed 100");
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) throw new IllegalArgumentException("INVALID_SORT:Sort field '" + sortBy + "' is not allowed. Allowed: name, description, category, createdDate");
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Item> resultPage = searchService.search(q, category, pageable);
        List<ItemDto> dtoList = resultPage.getContent().stream().map(item -> new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getCategory(), item.getCreatedDate())).collect(Collectors.toList());
        return ResponseEntity.ok(new SearchResponse(dtoList, resultPage.getNumber(), resultPage.getSize(), resultPage.getTotalElements(), resultPage.getTotalPages(), resultPage.isFirst(), resultPage.isLast()));
    }
}