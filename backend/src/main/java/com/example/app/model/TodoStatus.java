package com.example.app.model;

/**
 * Enum representing the possible statuses of a Todo item.
 * Used for filtering and categorizing todos by their current state.
 */
public enum TodoStatus {
    /**
     * Todo has been created but work has not started
     */
    PENDING,
    
    /**
     * Work on the todo is currently in progress
     */
    IN_PROGRESS,
    
    /**
     * Todo has been completed
     */
    COMPLETED
}
