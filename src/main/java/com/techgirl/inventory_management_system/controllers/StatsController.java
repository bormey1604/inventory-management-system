package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.StatsResponse;
import com.techgirl.inventory_management_system.services.SaleService;
import com.techgirl.inventory_management_system.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(StatsController.class);
    private final SaleService saleService;
    private final ProductService productService;

    @Autowired
    public StatsController(SaleService saleService, ProductService productService) {
        this.saleService = saleService;
        this.productService = productService;
    }

    @GetMapping
    public StatsResponse getStats() {
        logger.info("Fetching stats for total sales, revenue, products, and stock status.");

        StatsResponse statsResponse = new StatsResponse();

        statsResponse.setTotalSales(saleService.getTotalSales());
        statsResponse.setTotalRevenue(saleService.getTotalRevenue());
        statsResponse.setTotalProducts(productService.getTotalProducts());
        statsResponse.setTotalProductsInStock(productService.getTotalProductsInStock());
        statsResponse.setOutOfStockProducts(productService.getOutOfStockProducts());
        statsResponse.setLowStockProducts(productService.getLowStockProducts(10));  // assuming 10 as low stock threshold

        logger.info("Stats response: {}", statsResponse);
        return statsResponse;
    }

    @GetMapping("/sales/last7days")
    public ResponseEntity<List<Map<String,Object>>> getStatsSalesForLast7Days(){
        logger.info("Fetching sales data for the last 7 days.");
        List<Map<String,Object>> salesData = saleService.getSalesCountForLast7Days();
        logger.info("Sales data for last 7 days: {}", salesData);
        return new ResponseEntity<>(salesData, HttpStatus.OK);
    }

    @GetMapping("/revenue/monthly")
    public ResponseEntity<List<Map<String,Object>>> getMonthlyRevenue(){
        logger.info("Fetching monthly revenue data.");
        List<Map<String,Object>> monthlyRevenue = saleService.getMonthlyRevenue();
        logger.info("Monthly revenue data: {}", monthlyRevenue);
        return new ResponseEntity<>(monthlyRevenue, HttpStatus.OK);
    }
}
