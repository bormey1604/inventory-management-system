package com.techgirl.inventory_management_system.controllers;


import com.techgirl.inventory_management_system.dto.ProductDto;
import com.techgirl.inventory_management_system.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductsByCategory(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        Optional<ProductDto> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, String>> updateProduct(@PathVariable String id, @RequestBody ProductDto request) {
        try {
            String responseMessage = productService.updateProduct(id, request);
            Map<String, String> response = Map.of("message", responseMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Product update failed: " + e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }




}
