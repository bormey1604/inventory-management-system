package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.StatsResponse;
import com.techgirl.inventory_management_system.services.SaleService;
import com.techgirl.inventory_management_system.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {

    private final SaleService saleService;
    private final ProductService productService;

    @Autowired
    public StatsController(SaleService saleService, ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    @GetMapping
    public StatsResponse getStats() {
        StatsResponse statsResponse = new StatsResponse();

        statsResponse.setTotalSales(saleService.getTotalSales());
        statsResponse.setTotalRevenue(saleService.getTotalRevenue());
        statsResponse.setTotalProducts(productService.getTotalProducts());
        statsResponse.setTotalProductsInStock(productService.getTotalProductsInStock());
        statsResponse.setOutOfStockProducts(productService.getOutOfStockProducts());
        statsResponse.setLowStockProducts(productService.getLowStockProducts(10));  // assuming 10 as low stock threshold

        return statsResponse;
    }
}
