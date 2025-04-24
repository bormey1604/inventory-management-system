package com.techgirl.inventory_management_system.controllers;


import com.techgirl.inventory_management_system.dto.ProductDto;
import com.techgirl.inventory_management_system.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("api/v1/products")
@CrossOrigin("*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto product) {
        logger.info("Received request to add product: {}", product);
        ProductDto created = productService.createProduct(product);
        logger.info("Product created successfully: {}", created);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        logger.info("Fetching all products");
        List<ProductDto> products = productService.getAllProducts();
        logger.info("Found {} products", products.size());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getProducts(@PathVariable String id) {
        logger.info("Fetching products by category ID: {}", id);
        List<ProductDto> products = productService.getProductsByCategory(id);
        logger.info("Found {} products in category {}", products.size(), id);
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        logger.info("Fetching product by ID: {}", id);
        Optional<ProductDto> product = productService.getProductById(id);
        if (product.isPresent()) {
            logger.info("Product found: {}", product.get());
            return ResponseEntity.ok(product.get());
        } else {
            logger.warn("Product not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String id, @RequestBody ProductDto request) {
        logger.info("Updating product with ID: {} with data: {}", id, request);
        ProductDto updated = productService.updateProduct(id, request);
        logger.info("Product updated: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        logger.info("Deleting product with ID: {}", id);
        String result = productService.deleteProduct(id);
        logger.info("Product deleted: {}", result);
        return ResponseEntity.ok(result);
    }
}
