package com.techgirl.inventory_management_system.services;

import com.techgirl.inventory_management_system.dto.CategoryDto;
import com.techgirl.inventory_management_system.mapper.Mapper;
import com.techgirl.inventory_management_system.models.Category;
import com.techgirl.inventory_management_system.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = Mapper.toCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return Mapper.fromCategory(savedCategory);
    }

    public Optional<CategoryDto> getCategoryById(String id) {
        return categoryRepository.findById(id)
                .map(Mapper::fromCategory);
    }

    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(Mapper::fromCategory)
                .toList();
    }
}
