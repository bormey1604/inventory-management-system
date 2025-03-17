package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.CategoryDto;
import com.techgirl.inventory_management_system.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/categories")
@CrossOrigin("*")
public class CategoryController {
    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto category){
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String id){
        Optional<CategoryDto> findCategory = categoryService.getCategoryById(id);
        return findCategory.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
