package com.example.app.repository;

import com.example.app.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Todo entity with JPA Specification support.
 * JpaSpecificationExecutor enables dynamic queries via Specifications.
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {
}
