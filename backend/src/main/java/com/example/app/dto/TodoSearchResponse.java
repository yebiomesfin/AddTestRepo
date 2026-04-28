package com.example.app.dto;

import java.util.List;

public class TodoSearchResponse {

    private List<TodoItemDto> content;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public TodoSearchResponse(List<TodoItemDto> content, long totalElements,
                               int totalPages, int currentPage, int pageSize) {
        this.content = content;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getters
    public List<TodoItemDto> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
}
