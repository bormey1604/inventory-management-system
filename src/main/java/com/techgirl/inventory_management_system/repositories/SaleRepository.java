package com.techgirl.inventory_management_system.repositories;

import com.techgirl.inventory_management_system.models.Sale;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends MongoRepository<Sale,String> {
}
