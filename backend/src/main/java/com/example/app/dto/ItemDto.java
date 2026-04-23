package com.example.app.dto;

import java.time.LocalDateTime;

public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private LocalDateTime createdDate;
    public ItemDto() {}
    public ItemDto(Long id, String name, String description, String category, LocalDateTime createdDate) {
        this.id = id; this.name = name; this.description = description; this.category = category; this.createdDate = createdDate;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}