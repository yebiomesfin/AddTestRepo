package com.example.app.dto;

import com.example.app.model.Todo;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

/**
 * Response DTO for Todo search endpoint with pagination metadata.
 * Relates to Jira tickets: ADDAII-604, ADDAII-606, ADDAII-608
 */
public class TodoSearchResponse {

    private List<TodoItem> items;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    public TodoSearchResponse() {
    }

    /**
     * Factory method to create response from Spring Data Page.
     */
    public static TodoSearchResponse fromPage(Page<Todo> page) {
        TodoSearchResponse response = new TodoSearchResponse();
        response.items = page.getContent().stream()
                .map(TodoItem::fromEntity)
                .collect(Collectors.toList());
        response.totalElements = page.getTotalElements();
        response.totalPages = page.getTotalPages();
        response.currentPage = page.getNumber();
        response.pageSize = page.getSize();
        return response;
    }

    // Getters and Setters

    public List<TodoItem> getItems() {
        return items;
    }

    public void setItems(List<TodoItem> items) {
        this.items = items;
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Inner DTO representing a single Todo item in search results.
     */
    public static class TodoItem {
        private Long id;
        private String title;
        private String status;
        private LocalDate dueDate;
        private String description;

        public static TodoItem fromEntity(Todo todo) {
            TodoItem item = new TodoItem();
            item.id = todo.getId();
            item.title = todo.getTitle();
            item.status = todo.getStatus();
            item.dueDate = todo.getDueDate();
            item.description = todo.getDescription();
            return item;
        }

        // Getters and Setters

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public void setDueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}