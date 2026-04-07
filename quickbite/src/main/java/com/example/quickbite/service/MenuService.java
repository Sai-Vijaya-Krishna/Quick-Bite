package com.example.quickbite.service;

import com.example.quickbite.entity.MenuItem;
import com.example.quickbite.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<MenuItem> getAllAvailable() {
        return menuItemRepository.findByAvailableTrue();
    }

    public List<MenuItem> getAll() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> getByCategory(String category) {
        return menuItemRepository.findByCategory(category);
    }

    public MenuItem addItem(MenuItem item) {
        return menuItemRepository.save(item);
    }

    public MenuItem updateItem(Long id, MenuItem updated) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setName(updated.getName());
        item.setDescription(updated.getDescription());
        item.setPrice(updated.getPrice());
        item.setCategory(updated.getCategory());
        item.setImageUrl(updated.getImageUrl());
        item.setAvailable(updated.isAvailable());
        return menuItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        menuItemRepository.deleteById(id);
    }
}
