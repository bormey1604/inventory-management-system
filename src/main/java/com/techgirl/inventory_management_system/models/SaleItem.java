package com.techgirl.inventory_management_system.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItem {
    private String productId;
    private int quantity;
    private double price;
}
