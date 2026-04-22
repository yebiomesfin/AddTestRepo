package com.example.app.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * JPA Entity representing searchable items in the system
 * Table: items
 * 
 * @author Search API
 * @version 1.0
 * @see com.example.app.dto.ItemDTO
 */
@Entity
@Table(name = "items")
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(length = 100)
    private String category;
    
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
    
    // Constructors
    public Item() {
        this.createdDate = LocalDate.now();
    }
    
    public Item(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.createdDate = LocalDate.now();
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
