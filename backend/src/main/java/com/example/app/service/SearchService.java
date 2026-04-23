package com.example.app.service;

import com.example.app.model.Item;
import com.example.app.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    private final ItemRepository itemRepository;
    public SearchService(ItemRepository itemRepository) { this.itemRepository = itemRepository; }
    public Page<Item> search(String query, String category, Pageable pageable) {
        String normalizedQuery = query == null ? "" : query.toLowerCase().trim();
        String normalizedCategory = (category == null || category.isBlank()) ? null : category.trim();
        return itemRepository.searchItems(normalizedQuery, normalizedCategory, pageable);
    }
}