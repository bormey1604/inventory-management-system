package com.techgirl.inventory_management_system.services;

import com.techgirl.inventory_management_system.dto.SaleRequestDto;
import com.techgirl.inventory_management_system.models.ProductItem;
import com.techgirl.inventory_management_system.models.Sale;
import com.techgirl.inventory_management_system.models.SaleItem;
import com.techgirl.inventory_management_system.repositories.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;

    public SaleService(SaleRepository saleRepository, ProductService productService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
    }

    //Get Total Sales
    public long getTotalSales() {
        return saleRepository.count();
    }

    // Get Total Revenue
    public double getTotalRevenue() {
        return saleRepository.findAll().stream()
                .mapToDouble(Sale::getTotalAmount)
                .sum();
    }

    public Sale createSale(SaleRequestDto requestDto) {
        List<SaleItem> saleItems = requestDto.getSaleItems();
        List<ProductItem> inventoryItems = productService.getProductItems();

        //validate stock availability
        List<SaleItem> outOfStockItems = saleItems.stream()
                .filter(saleItem -> inventoryItems.stream()
                        .filter(invItem -> invItem.getProductId().equals(saleItem.getProductId()))
                        .findFirst()
                        .map(invItem -> saleItem.getQuantity() > invItem.getQuantity())
                        .orElse(true)
                )
                .toList();

        if (!outOfStockItems.isEmpty()) {
            throw new IllegalArgumentException("Some products are out of stock: " + outOfStockItems);
        }

        saleItems.forEach(saleItem ->
                productService.updateProductStock(saleItem.getProductId(), saleItem.getQuantity(), false)
        );

        double totalAmount = saleItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double discountPercentage = requestDto.getDiscountPercentage() != null ? requestDto.getDiscountPercentage() : 0.0;

        double percentageDiscountValue = (discountPercentage / 100) * totalAmount;
        double finalAmount = totalAmount - percentageDiscountValue;

        finalAmount = Math.max(finalAmount, 0.0);

        Sale sale = new Sale();
        sale.setCustomerId(requestDto.getCustomerId());
        sale.setSaleItems(saleItems);
        sale.setTotalAmount(totalAmount);
        sale.setDiscountPercentage(discountPercentage);
        sale.setFinalAmount(finalAmount);
        sale.setPaymentMethod(requestDto.getPaymentMethod());
        sale.setCreatedAt(String.valueOf(LocalDateTime.now()));
        sale.setUpdatedAt(String.valueOf(LocalDateTime.now()));

        return saleRepository.save(sale);
    }

}
