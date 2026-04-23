package com.example.app.service;

import com.example.app.model.Item;
import com.example.app.repository.ItemRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {
    @Mock private ItemRepository itemRepository;
    @InjectMocks private SearchService searchService;
    @BeforeEach void setUp() { MockitoAnnotations.openMocks(this); }

    @Test void search_withKeyword_returnsMatchingItems() {
        Item item = new Item(); item.setId(1L); item.setName("Laptop Pro"); item.setDescription("High-performance laptop"); item.setCategory("electronics"); item.setCreatedDate(LocalDateTime.now());
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        Page<Item> mockPage = new PageImpl<>(List.of(item), pageable, 1);
        when(itemRepository.searchItems("laptop", null, pageable)).thenReturn(mockPage);
        Page<Item> result = searchService.search("laptop", null, pageable);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Laptop Pro");
    }

    @Test void search_withCategoryFilter_returnsFilteredItems() {
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRepository.searchItems("laptop", "accessories", pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));
        assertThat(searchService.search("laptop", "accessories", pageable).getTotalElements()).isEqualTo(0);
    }

    @Test void search_withBlankCategory_passesNullToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        when(itemRepository.searchItems("test", null, pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 0));
        searchService.search("test", "  ", pageable);
        verify(itemRepository).searchItems("test", null, pageable);
    }

    @Test void search_withPagination_returnsCorrectMetadata() {
        Pageable pageable = PageRequest.of(0, 5);
        when(itemRepository.searchItems("test", null, pageable)).thenReturn(new PageImpl<>(List.of(), pageable, 50));
        Page<Item> result = searchService.search("test", null, pageable);
        assertThat(result.getTotalPages()).isEqualTo(10);
    }
}