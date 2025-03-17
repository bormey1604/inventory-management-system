package com.techgirl.inventory_management_system.services;

import com.techgirl.inventory_management_system.dto.ProductDto;
import com.techgirl.inventory_management_system.mapper.Mapper;
import com.techgirl.inventory_management_system.models.Category;
import com.techgirl.inventory_management_system.models.Product;
import com.techgirl.inventory_management_system.models.ProductItem;
import com.techgirl.inventory_management_system.models.SaleItem;
import com.techgirl.inventory_management_system.repositories.CategoryRepository;
import com.techgirl.inventory_management_system.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDto> getProductsByCategory(String categoryId) {
        return productRepository.findAllByCategoryId(categoryId)
                .stream()
                .map(Mapper::fromProduct)
                .toList();
    }

    public ProductDto createProduct(ProductDto productDto) {
        String categoryId = productDto.getCategoryId();
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (categoryOptional.isPresent()) {
            Product product = Mapper.toProduct(productDto);
            Product savedProduct = productRepository.save(product);

            Category category = categoryOptional.get();
            category.getProducts().add(savedProduct);
            categoryRepository.save(category);

            return Mapper.fromProduct(savedProduct);
        }

        return null;
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(Mapper::fromProduct)
                .toList();
    }

    public Optional<ProductDto> getProductById(String id) {
        return productRepository.findById(id)
                .map(Mapper::fromProduct);
    }

    public String updateProduct(String id, ProductDto productDto) {
        Optional<Product> existingProductOptional = productRepository.findById(id);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            existingProduct.setName(productDto.getName());
            existingProduct.setPrice(productDto.getPrice());
            existingProduct.setDescription(productDto.getDescription());
            existingProduct.setStock(productDto.getStock());

            productRepository.save(existingProduct);

            Optional<Category> categoryOptional = categoryRepository.findById(existingProduct.getCategoryId());

            if (categoryOptional.isPresent()) {
                Category category = categoryOptional.get();

                for (int i = 0; i < category.getProducts().size(); i++) {
                    Product categoryProduct = category.getProducts().get(i);
                    if (categoryProduct.getId().equals(id)) {
                        category.getProducts().set(i, existingProduct);
                        break;
                    }
                }
                categoryRepository.save(category);
                return "Product and Category updated successfully";
            } else {
                return "Category not found";
            }

        } else {
            return "Product not found";
        }
    }

    public String deleteProduct(String id) {
        Optional<Product> existingProductOptional = productRepository.findById(id);

        if (existingProductOptional.isPresent()) {
            Product product = existingProductOptional.get();

            Optional<Category> categoryOptional = categoryRepository.findById(product.getCategoryId());
            categoryOptional.ifPresent(category -> {
                category.getProducts().removeIf(p -> p.getId().equals(id));
                categoryRepository.save(category);
            });

            productRepository.deleteById(id);
            return "Product deleted successfully";
        } else {
            return "Product not found";
        }
    }

    public List<ProductItem> getProductItems(){
        return getAllProducts().stream().map(
                item -> {
                    ProductItem productItem = new ProductItem();

                    productItem.setProductId(item.getId());
                    productItem.setQuantity(item.getStock());
                    productItem.setPrice(item.getPrice());
                    return productItem;
                }
        ).toList();
    }

    public String updateProductStock(String id, int stockChange, boolean isAddition) {
        Optional<Product> existingProductOptional = productRepository.findById(id);

        if (existingProductOptional.isPresent()) {
            Product existingProduct = existingProductOptional.get();

            if (isAddition) {
                existingProduct.setStock(existingProduct.getStock() + stockChange);
            } else {
                if (existingProduct.getStock() >= stockChange) {
                    existingProduct.setStock(existingProduct.getStock() - stockChange);
                } else {
                    return "Insufficient stock for this purchase";
                }
            }

            productRepository.save(existingProduct);

            Optional<Category> categoryOptional = categoryRepository.findById(existingProduct.getCategoryId());

            if (categoryOptional.isPresent()) {
                Category category = categoryOptional.get();

                for (int i = 0; i < category.getProducts().size(); i++) {
                    Product categoryProduct = category.getProducts().get(i);
                    if (categoryProduct.getId().equals(id)) {
                        category.getProducts().set(i, existingProduct);
                        break;
                    }
                }

                categoryRepository.save(category);
                return isAddition ? "Stock added successfully" : "Stock deducted successfully";
            } else {
                return "Category not found";
            }

        } else {
            return "Product not found";
        }
    }

    public int getTotalProducts() {
        List<ProductItem> inventoryItems = getProductItems();
        return inventoryItems.size();
    }

    // Get Total Products in Stock
    public int getTotalProductsInStock() {
        return productRepository.findAll().stream()
                .mapToInt(Product::getStock)
                .sum();
    }

    // Get Out of Stock Products
    public long getOutOfStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getStock() == 0)
                .count();
    }

    // Get Low Stock Products
    public long getLowStockProducts(int threshold) {
        return productRepository.findAll().stream()
                .filter(productItem -> productItem.getStock() < threshold)
                .count();
    }


}
