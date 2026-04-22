package com.example.app.dto;

import java.util.List;

/**
 * DTO wrapper for paginated search API responses
 * Contains result data and pagination metadata
 * 
 * @author Search API
 * @version 1.0
 * @see ItemDTO
 * @see com.example.app.controller.SearchController
 */
public class SearchResponseDTO {
    
    private List<ItemDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private String sortBy;
    private String sortDir;
    
    public SearchResponseDTO() {
    }
    
    public SearchResponseDTO(List<ItemDTO> content, int page, int size, long totalElements, 
                              int totalPages, String sortBy, String sortDir) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.sortBy = sortBy;
        this.sortDir = sortDir;
    }
    
    // Getters and Setters
    public List<ItemDTO> getContent() {
        return content;
    }
    
    public void setContent(List<ItemDTO> content) {
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
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getSortDir() {
        return sortDir;
    }
    
    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }
}
