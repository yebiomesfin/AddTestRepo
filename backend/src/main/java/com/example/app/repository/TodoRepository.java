package com.example.app.repository;

import com.example.app.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Todo entity.
 * Extended with JpaSpecificationExecutor for dynamic query support (ADDAII-603, ADDAII-604, ADDAII-605).
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {
    List<Todo> findByCompleted(boolean completed);
}