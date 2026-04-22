package com.example.app.controller;

import com.example.app.model.Item;
import com.example.app.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for SearchController
 * Tests full request/response flow with real Spring context
 * 
 * @author Search API
 * @version 1.0
 * @see SearchController
 */
@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        // Clear existing data
        itemRepository.deleteAll();

        // Seed test data
        Item item1 = new Item("Spring Boot Guide", "Comprehensive guide to Spring Boot framework", "Books");
        item1.setCreatedDate(LocalDate.of(2026, 1, 15));

        Item item2 = new Item("Java Toolkit", "Essential Java development tools", "Tools");
        item2.setCreatedDate(LocalDate.of(2026, 2, 20));

        Item item3 = new Item("Spring Security Handbook", "Security best practices for Spring applications", "Books");
        item3.setCreatedDate(LocalDate.of(2026, 3, 10));

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
    }

    @Test
    void testSearchWithKeyword_ReturnsMatchingItems() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("q", "spring")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20));
    }

    @Test
    void testSearchWithCategory_ReturnsFilteredItems() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("category", "Books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].category").value("Books"));
    }

    @Test
    void testSearchWithKeywordAndCategory_ReturnsCombinedFilter() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("q", "security")
                .param("category", "Books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Spring Security Handbook"));
    }

    @Test
    void testSearchWithPagination_ReturnsCorrectPage() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("page", "0")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));
    }

    @Test
    void testSearchWithSorting_ReturnsSortedResults() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("sortBy", "name")
                .param("sortDir", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sortBy").value("name"))
                .andExpect(jsonPath("$.sortDir").value("asc"))
                .andExpect(jsonPath("$.content[0].name").value("Java Toolkit"));
    }

    @Test
    void testSearchWithDefaults_AppliesDefaultPaginationAndSorting() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.sortBy").value("createdDate"))
                .andExpect(jsonPath("$.sortDir").value("desc"));
    }

    @Test
    void testSearchWithNoResults_ReturnsEmptyContent() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("q", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void testSearchWithInvalidPage_Returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("page", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Page index must not be less than zero"));
    }

    @Test
    void testSearchWithInvalidPageSize_Returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("size", "500")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testSearchWithInvalidSortBy_Returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("sortBy", "invalidField")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testSearchWithInvalidSortDir_Returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search")
                .param("sortDir", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }
}
