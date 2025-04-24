package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.CategoryDto;
import com.techgirl.inventory_management_system.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/categories")
@CrossOrigin("*")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto category){
        logger.info("Received request to create category: {}", category);
        CategoryDto created = categoryService.createCategory(category);
        logger.info("Category created: {}", created);
        return ResponseEntity.ok(created);
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String id){
        logger.info("Fetching category by ID: {}", id);
        Optional<CategoryDto> findCategory = categoryService.getCategoryById(id);
        if (findCategory.isPresent()) {
            logger.info("Category found: {}", findCategory.get());
            return ResponseEntity.ok(findCategory.get());
        } else {
            logger.warn("Category not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(){
        logger.info("Fetching all categories");
        List<CategoryDto> categories = categoryService.getCategories();
        logger.info("Found {} categories", categories.size());
        return ResponseEntity.ok(categories);
    }
}
