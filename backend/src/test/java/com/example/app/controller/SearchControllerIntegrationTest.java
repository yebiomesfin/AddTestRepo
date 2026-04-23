package com.example.app.controller;

import com.example.app.model.Item;
import com.example.app.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private SearchService searchService;

    @Test void search_validRequest_returns200() throws Exception {
        Item item = new Item(); item.setId(1L); item.setName("Laptop Pro"); item.setDescription("A great laptop"); item.setCategory("electronics"); item.setCreatedDate(LocalDateTime.now());
        when(searchService.search(eq("laptop"), eq("electronics"), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(item), PageRequest.of(0,10), 1));
        mockMvc.perform(get("/api/v1/search").param("q","laptop").param("category","electronics").param("page","0").param("size","10").param("sortBy","name").param("direction","asc"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(1)).andExpect(jsonPath("$.content[0].name").value("Laptop Pro"));
    }
    @Test void search_missingQ_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("MISSING_QUERY"));
    }
    @Test void search_qTooShort_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search").param("q","a")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_QUERY"));
    }
    @Test void search_sizeExceeds100_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search").param("q","test").param("size","500")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_PAGINATION"));
    }
    @Test void search_invalidSortBy_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/search").param("q","test").param("sortBy","password")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.code").value("INVALID_SORT"));
    }
    @Test void search_noResults_returns200WithEmptyContent() throws Exception {
        when(searchService.search(eq("xyznonexistent"), isNull(), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(), PageRequest.of(0,20), 0));
        mockMvc.perform(get("/api/v1/search").param("q","xyznonexistent")).andExpect(status().isOk()).andExpect(jsonPath("$.totalElements").value(0)).andExpect(jsonPath("$.content").isEmpty());
    }
}