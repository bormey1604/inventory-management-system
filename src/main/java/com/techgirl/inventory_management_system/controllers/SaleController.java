package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.SaleRequestDto;
import com.techgirl.inventory_management_system.models.Sale;
import com.techgirl.inventory_management_system.services.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/sales")
@CrossOrigin("*")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody SaleRequestDto requestDto){
        return new ResponseEntity<>(saleService.createSale(requestDto), HttpStatus.OK);
    }
}
