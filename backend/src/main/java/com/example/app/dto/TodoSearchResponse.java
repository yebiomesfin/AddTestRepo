package com.example.app.dto;

import java.util.List;

/**
 * Paginated response wrapper for Todo search results.
 * Contains the search results along with pagination metadata.
 */
public class TodoSearchResponse {
    private List<TodoDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    // Constructors
    public TodoSearchResponse() {
    }

    public TodoSearchResponse(List<TodoDTO> content, int page, int size, 
                             long totalElements, int totalPages, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    // Getters and Setters
    public List<TodoDTO> getContent() {
        return content;
    }

    public void setContent(List<TodoDTO> content) {
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

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}
