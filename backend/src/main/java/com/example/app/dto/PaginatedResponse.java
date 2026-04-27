package com.example.app.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Generic paginated response wrapper for API endpoints.
 * Provides consistent pagination metadata across all paginated responses.
 *
 * @param <T> the type of content in the response
 */
public class PaginatedResponse<T> {

    /**
     * List of items in the current page
     */
    private List<T> content;

    /**
     * Current page index (0-based)
     */
    private int page;

    /**
     * Number of items per page
     */
    private int size;

    /**
     * Total number of items across all pages
     */
    private long totalElements;

    /**
     * Total number of pages
     */
    private int totalPages;

    // Constructors
    public PaginatedResponse() {
    }

    public PaginatedResponse(List<T> content, int page, int size, long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    /**
     * Factory method to create PaginatedResponse from Spring Data Page with entity-to-DTO mapping
     *
     * @param page   Spring Data Page containing entities
     * @param mapper Function to map entity to DTO
     * @param <E>    Entity type
     * @param <T>    DTO type
     * @return PaginatedResponse containing mapped DTOs
     */
    public static <E, T> PaginatedResponse<T> of(Page<E> page, Function<E, T> mapper) {
        List<T> content = page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    // Getters and Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
