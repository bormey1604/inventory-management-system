package com.techgirl.inventory_management_system.dto;

import lombok.Data;

@Data
public class StatsResponse {
    private long totalSales;
    private double totalRevenue;
    private int totalProducts;
    private int totalProductsInStock;
    private long outOfStockProducts;
    private long lowStockProducts;

}
