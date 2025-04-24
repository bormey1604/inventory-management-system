package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.SaleRequestDto;
import com.techgirl.inventory_management_system.models.Sale;
import com.techgirl.inventory_management_system.services.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/sales")
@CrossOrigin("*")
public class SaleController {

    private static final Logger logger = LoggerFactory.getLogger(SaleController.class);
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequestDto requestDto){
        logger.info("Received request to create sale: {}", requestDto);
        Sale createdSale = saleService.createSale(requestDto);
        logger.info("Created sale: {}", createdSale);
        return new ResponseEntity<>(createdSale, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getSales(){
        logger.info("Fetching all sales");
        List<Sale> sales = saleService.getSales();
        logger.info("Found {} sales", sales.size());
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String,Object>> getSalesByDate(@RequestParam("date") String date){
        logger.info("Fetching sales summary by date: {}", date);
        Map<String,Object> summary = saleService.getSalesSummaryByDate(date);
        logger.info("Sales summary: {}", summary);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }
}
