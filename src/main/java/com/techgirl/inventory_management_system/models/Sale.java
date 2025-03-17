
package com.techgirl.inventory_management_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "sales")
public class Sale {

    @Id
    private String saleId;

    private String customerId;
    private List<SaleItem> saleItems;
    private double totalAmount;
    private double discountPercentage;
    private double finalAmount;
    private String paymentMethod;

    @CreatedDate
    private String createdAt;

    @LastModifiedDate
    private String updatedAt;

}

