package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.StatsResponse;
import com.techgirl.inventory_management_system.services.SaleService;
import com.techgirl.inventory_management_system.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stats")
@CrossOrigin("*")
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

    @GetMapping("/sales/last7days")
    public ResponseEntity<List<Map<String,Object>>> getStatsSalesForLast7Days(){
        return new ResponseEntity<>(saleService.getSalesCountForLast7Days(), HttpStatus.OK);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<Map<String,Object>>> getMonthlyRevenue(){
        return new ResponseEntity<>(saleService.getMonthlyRevenue(), HttpStatus.OK);
    }
}
