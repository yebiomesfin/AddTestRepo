package com.example.app.service;

import com.example.app.dto.SearchResponseDTO;
import com.example.app.model.Item;
import com.example.app.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for SearchService
 * Tests keyword search, filtering, pagination, sorting, and validation logic
 * 
 * @author Search API
 * @version 1.0
 * @see SearchService
 */
@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private SearchService searchService;

    private List<Item> mockItems;

    @BeforeEach
    void setUp() {
        Item item1 = new Item("Spring Boot Guide", "Comprehensive guide to Spring Boot", "Books");
        item1.setId(1L);
        item1.setCreatedDate(LocalDate.of(2026, 1, 15));

        Item item2 = new Item("Java Toolkit", "Essential Java development tools", "Tools");
        item2.setId(2L);
        item2.setCreatedDate(LocalDate.of(2026, 2, 20));

        mockItems = Arrays.asList(item1, item2);
    }

    @Test
    void testSearchWithKeyword_ReturnsMatchingItems() {
        // Given
        Page<Item> mockPage = new PageImpl<>(mockItems.subList(0, 1));
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        // When
        SearchResponseDTO result = searchService.search("spring", null, 0, 20, "createdDate", "desc");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Spring Boot Guide", result.getContent().get(0).getName());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testSearchWithCategory_ReturnsFilteredItems() {
        // Given
        Page<Item> mockPage = new PageImpl<>(mockItems.subList(0, 1));
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        // When
        SearchResponseDTO result = searchService.search(null, "Books", 0, 20, "createdDate", "desc");

        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Books", result.getContent().get(0).getCategory());
    }

    @Test
    void testSearchWithPagination_ReturnsCorrectPage() {
        // Given
        Page<Item> mockPage = new PageImpl<>(mockItems, org.springframework.data.domain.PageRequest.of(0, 10), 25);
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        // When
        SearchResponseDTO result = searchService.search(null, null, 0, 10, "createdDate", "desc");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(25, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
    }

    @Test
    void testSearchWithSorting_AppliesCorrectSort() {
        // Given
        Page<Item> mockPage = new PageImpl<>(mockItems);
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        // When
        SearchResponseDTO result = searchService.search(null, null, 0, 20, "name", "asc");

        // Then
        assertNotNull(result);
        assertEquals("name", result.getSortBy());
        assertEquals("asc", result.getSortDir());
    }

    @Test
    void testSearchWithDefaults_AppliesDefaultValues() {
        // Given
        Page<Item> mockPage = new PageImpl<>(mockItems);
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        // When
        SearchResponseDTO result = searchService.search(null, null, null, null, null, null);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getPage());
        assertEquals(20, result.getSize());
        assertEquals("createdDate", result.getSortBy());
        assertEquals("desc", result.getSortDir());
    }

    @Test
    void testSearchWithInvalidPage_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            searchService.search(null, null, -1, 20, "createdDate", "desc");
        });
        assertEquals("Page index must not be less than zero", exception.getMessage());
    }

    @Test
    void testSearchWithInvalidPageSize_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            searchService.search(null, null, 0, 500, "createdDate", "desc");
        });
        assertTrue(exception.getMessage().contains("Page size must be between 1 and 100"));
    }

    @Test
    void testSearchWithInvalidSortBy_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            searchService.search(null, null, 0, 20, "invalidField", "desc");
        });
        assertTrue(exception.getMessage().contains("Invalid sortBy field"));
    }

    @Test
    void testSearchWithInvalidSortDir_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            searchService.search(null, null, 0, 20, "createdDate", "invalid");
        });
        assertTrue(exception.getMessage().contains("Invalid sortDir"));
    }

    @Test
    void testSearchWithNoResults_ReturnsEmptyContent() {
        // Given
        Page<Item> emptyPage = new PageImpl<>(List.of());
        when(itemRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        // When
        SearchResponseDTO result = searchService.search("nonexistent", null, 0, 20, "createdDate", "desc");

        // Then
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
    }
}
