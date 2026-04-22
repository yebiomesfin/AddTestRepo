package com.example.app.repository;

import com.example.app.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Item entity
 * Extends JpaSpecificationExecutor for dynamic query support via JPA Specifications
 * 
 * @author Search API
 * @version 1.0
 * @see Item
 * @see com.example.app.specification.ItemSpecification
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
    // Custom query methods can be added here if needed
    // JpaSpecificationExecutor provides dynamic filtering via Specification API
}
