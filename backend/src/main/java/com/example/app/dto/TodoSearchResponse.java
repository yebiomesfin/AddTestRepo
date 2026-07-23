package com.example.app.dto;

import com.example.app.model.Todo;

import java.util.List;

public class TodoSearchResponse {

    private List<Todo> items;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

    public TodoSearchResponse() {
    }

    public TodoSearchResponse(List<Todo> items, Long totalElements, Integer totalPages, Integer currentPage, Integer pageSize) {
        this.items = items;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    // Getters and Setters
    public List<Todo> getItems() {
        return items;
    }

    public void setItems(List<Todo> items) {
        this.items = items;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
