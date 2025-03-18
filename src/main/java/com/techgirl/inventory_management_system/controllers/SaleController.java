package com.techgirl.inventory_management_system.controllers;

import com.techgirl.inventory_management_system.dto.SaleRequestDto;
import com.techgirl.inventory_management_system.models.Sale;
import com.techgirl.inventory_management_system.services.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    public ResponseEntity<List<Sale>> getSales(){
        return new ResponseEntity<>(saleService.getSales(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String,Object>> getSalesByDate(@RequestParam("date") String date){
        return new ResponseEntity<>(saleService.getSalesSummaryByDate(date), HttpStatus.OK);
    }



}
