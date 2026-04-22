# Search API Documentation

## Overview

The Search API provides a RESTful endpoint for searching items with keyword filtering, category filtering, pagination, and sorting capabilities.

**Epic:** [ADDAII-407](https://acuity-inc.atlassian.net/browse/ADDAII-407) — Search API for AddTestRepo Backend

**Related Stories:**
- [ADDAII-408](https://acuity-inc.atlassian.net/browse/ADDAII-408) — Keyword search endpoint
- [ADDAII-409](https://acuity-inc.atlassian.net/browse/ADDAII-409) — Pagination support
- [ADDAII-410](https://acuity-inc.atlassian.net/browse/ADDAII-410) — Sorting support
- [ADDAII-411](https://acuity-inc.atlassian.net/browse/ADDAII-411) — Category filter
- [ADDAII-412](https://acuity-inc.atlassian.net/browse/ADDAII-412) — Input validation and error handling
- [ADDAII-413](https://acuity-inc.atlassian.net/browse/ADDAII-413) — Unit and integration tests

---

## Endpoint Specification

### `GET /api/v1/search`

Search for items with optional filters, pagination, and sorting.

#### Query Parameters

| Parameter | Type | Required | Default | Constraints | Description |
|---|---|---|---|---|---|
| `q` | String | No | `null` | Max 255 chars | Keyword search (case-insensitive partial match on `name` and `description`) |
| `category` | String | No | `null` | Max 100 chars | Exact category filter |
| `page` | Integer | No | `0` | ≥ 0 | Zero-indexed page number |
| `size` | Integer | No | `20` | 1–100 | Number of results per page |
| `sortBy` | String | No | `createdDate` | `name`, `description`, `category`, `createdDate` | Field to sort by |
| `sortDir` | String | No | `desc` | `asc`, `desc` | Sort direction |

---

## Request Examples

### Basic keyword search
```bash
GET /api/v1/search?q=spring
```

### Search with category filter
```bash
GET /api/v1/search?q=java&category=Books
```

### Search with pagination
```bash
GET /api/v1/search?page=0&size=10
```

### Search with sorting
```bash
GET /api/v1/search?sortBy=name&sortDir=asc
```

### Combined filters
```bash
GET /api/v1/search?q=security&category=Books&page=0&size=20&sortBy=createdDate&sortDir=desc
```

---

## Response Format

### Success Response (HTTP 200)

```json
{
  "content": [
    {
      "id": 1,
      "name": "Spring Boot Guide",
      "description": "A comprehensive guide to Spring Boot",
      "category": "Books",
      "createdDate": "2026-01-15"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 42,
  "totalPages": 3,
  "sortBy": "createdDate",
  "sortDir": "desc"
}
```

### Error Response (HTTP 400)

```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "Page index must not be less than zero",
  "timestamp": "2026-04-22T15:00:00Z"
}
```

### Error Response (HTTP 500)

```json
{
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred. Please try again later.",
  "timestamp": "2026-04-22T15:00:00Z"
}
```

---

## Architecture

### Package Structure

```
com.example.app
├── controller
│   └── SearchController.java        # REST endpoint handler
├── service
│   └── SearchService.java           # Business logic and validation
├── repository
│   └── ItemRepository.java          # JPA repository with JpaSpecificationExecutor
├── model
│   └── Item.java                    # JPA entity
├── dto
│   ├── ItemDTO.java                 # Response item DTO
│   ├── SearchResponseDTO.java       # Paginated response wrapper
│   └── ErrorResponseDTO.java        # Structured error response
├── specification
│   └── ItemSpecification.java       # JPA Specification for dynamic filtering
└── exception
    └── GlobalExceptionHandler.java  # Centralized error handling
```

### Data Flow

```
Client Request
    ↓
SearchController (validates parameters)
    ↓
SearchService (builds Specification + Pageable)
    ↓
ItemRepository (executes JPA query)
    ↓
SearchService (maps entities to DTOs)
    ↓
SearchController (returns JSON response)
```

---

## Database Schema

### `items` Table

| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique item identifier |
| `name` | VARCHAR(255) | NOT NULL | Item name |
| `description` | TEXT | NULL | Item description |
| `category` | VARCHAR(100) | NULL | Item category |
| `created_date` | DATE | NOT NULL | Creation date |

---

## Testing

### Run Unit Tests
```bash
mvn test -Dtest=SearchServiceTest
```

### Run Integration Tests
```bash
mvn test -Dtest=SearchControllerIntegrationTest
```

### Run All Tests
```bash
mvn test
```

### Test Coverage
- **Unit Tests:** `SearchServiceTest` — 10 test cases covering validation, filtering, pagination, sorting
- **Integration Tests:** `SearchControllerIntegrationTest` — 12 test cases covering HTTP request/response scenarios

**Expected Coverage:** ≥80% line coverage

---

## Validation Rules

| Rule | Error Message |
|---|---|
| `page < 0` | "Page index must not be less than zero" |
| `size < 1 or size > 100` | "Page size must be between 1 and 100" |
| `sortBy` not in allowed list | "Invalid sortBy field. Allowed values: name, description, category, createdDate" |
| `sortDir` not `asc` or `desc` | "Invalid sortDir. Allowed values: asc, desc" |
| `keyword` > 255 chars | "Keyword must not exceed 255 characters" |
| `category` > 100 chars | "Category must not exceed 100 characters" |

---

## Performance Considerations

- **Expected Response Time:** ≤ 500ms for up to 100,000 records
- **Recommended Indexes:**
  ```sql
  CREATE INDEX idx_items_name ON items(name);
  CREATE INDEX idx_items_category ON items(category);
  CREATE INDEX idx_items_created_date ON items(created_date);
  ```

---

## Future Enhancements (Out of Scope for MVP)

- Full-text search with Elasticsearch integration
- Multiple category filters (e.g., `category=Books,Tools`)
- Date range filtering (e.g., `createdAfter=2026-01-01&createdBefore=2026-12-31`)
- Authentication and authorization
- Rate limiting and throttling

---

## Related Documentation

- **Jira Epic:** [ADDAII-407](https://acuity-inc.atlassian.net/browse/ADDAII-407)
- **Architecture Reference:** [Spring Data JPA Specifications](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications)
- **Error Handling:** See `GlobalExceptionHandler.java`
- **Testing Strategy:** See `/backend/src/test/java/com/example/app/`

---

## Support

For questions or issues, please contact the Search API development team or create a Jira ticket in project **ADDAII**.

**Version:** 1.0  
**Last Updated:** 2026-04-22  
**Maintainer:** Search API Team
