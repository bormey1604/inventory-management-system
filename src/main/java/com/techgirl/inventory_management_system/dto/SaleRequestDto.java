package com.techgirl.inventory_management_system.dto;

import com.techgirl.inventory_management_system.models.SaleItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaleRequestDto {

    private String customerId;
    private List<SaleItem> saleItems;
    private double totalAmount;
    private String paymentMethod;
    private Double discountPercentage;

}

