package com.techgirl.inventory_management_system.repositories;

import com.techgirl.inventory_management_system.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findAllByCategoryId(String categoryId);
}
