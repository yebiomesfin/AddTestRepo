package com.example.app.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object for Item entity
 * Used for API responses to decouple internal entity structure from API contracts
 * 
 * @author Search API
 * @version 1.0
 * @see com.example.app.model.Item
 */
public class ItemDTO {
    
    private Long id;
    private String name;
    private String description;
    private String category;
    private LocalDate createdDate;
    
    public ItemDTO() {
    }
    
    public ItemDTO(Long id, String name, String description, String category, LocalDate createdDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.createdDate = createdDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
