package com.example.app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TodoSearchRequest {

    private static final List<String> ALLOWED_SORT_FIELDS = Arrays.asList("title", "dueDate", "createdAt", "status");
    private static final List<String> VALID_STATUSES = Arrays.asList("PENDING", "IN_PROGRESS", "COMPLETED");

    private String title;

    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDateTo;

    @Min(value = 0, message = "page must be >= 0")
    private int page = 0;

    @Min(value = 1, message = "size must be between 1 and 100")
    @Max(value = 100, message = "size must be between 1 and 100")
    private int size = 10;

    private String sort = "dueDate,asc";

    // --- Validation helpers ---

    public boolean hasValidStatus() {
        return status == null || VALID_STATUSES.contains(status.toUpperCase());
    }

    public boolean hasValidSortField() {
        if (sort == null) return true;
        String field = sort.split(",")[0].trim();
        return ALLOWED_SORT_FIELDS.contains(field);
    }

    public boolean isDateRangeValid() {
        if (dueDateFrom != null && dueDateTo != null) {
            return !dueDateFrom.isAfter(dueDateTo);
        }
        return true;
    }

    public static List<String> getAllowedSortFields() {
        return ALLOWED_SORT_FIELDS;
    }

    public static List<String> getValidStatuses() {
        return VALID_STATUSES;
    }

    // --- Getters and Setters ---

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDueDateFrom() { return dueDateFrom; }
    public void setDueDateFrom(LocalDate dueDateFrom) { this.dueDateFrom = dueDateFrom; }

    public LocalDate getDueDateTo() { return dueDateTo; }
    public void setDueDateTo(LocalDate dueDateTo) { this.dueDateTo = dueDateTo; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
}
