package com.techgirl.inventory_management_system.repositories;

import com.techgirl.inventory_management_system.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category,String> {
}
