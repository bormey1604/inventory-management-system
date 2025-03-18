package com.techgirl.inventory_management_system.services;

import com.techgirl.inventory_management_system.dto.SaleRequestDto;
import com.techgirl.inventory_management_system.mapper.Mapper;
import com.techgirl.inventory_management_system.models.ProductItem;
import com.techgirl.inventory_management_system.models.Sale;
import com.techgirl.inventory_management_system.models.SaleItem;
import com.techgirl.inventory_management_system.repositories.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public List<Sale> getSales(){
        return saleRepository.findAll();
    }

    public List<Sale> getSalesByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME; // Assuming date is in ISO format like "2025-03-17T09:57:39.636934900"

        return getSales().stream()
                .filter(sale -> {

                    if (sale.getCreatedAt() != null) {
                        LocalDateTime saleDate = LocalDateTime.parse(sale.getCreatedAt(), formatter);
                        return saleDate.toLocalDate().toString().equals(date);
                    }
                    return false;
                })
                .toList();
    }

    public Map<String,Object> getSalesSummaryByDate(String date) {

        return Map.of("totalCount", getSalesByDate(date).size(),
                "sales",getSalesByDate(date));


    }

    public List<Map<String, Object>> getSalesCountForLast7Days() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Generate the last 7 days (including today)
        List<LocalDate> last7Days = IntStream.range(0, 7)
                .mapToObj(currentDate::minusDays)
                .collect(Collectors.toList());

        // Collect total sales count per day and return as a list of maps
        return last7Days.stream()
                .map(date -> {
                    Map<String, Object> salesData = Map.of(
                            "date", date.toString(),
                            "totalSales", (long) getSalesByDate(date.toString()).size()
                    );
                    return salesData;
                })
                .toList();
    }


    public List<Map<String, Object>> getMonthlyRevenue() {
        List<Sale> sales = saleRepository.findAll();

        return sales.stream()
                .collect(Collectors.groupingBy(
                        sale -> sale.getCreatedAt().substring(0, 7), // Extract month as "YYYY-MM"
                        Collectors.summingDouble(Sale::getTotalAmount) // Sum totalAmount for each month
                ))
                .entrySet().stream()
                .map(entry -> {
                    // Use a HashMap to ensure correct types
                    Map<String, Object> monthRevenue = new HashMap<>();
                    monthRevenue.put("month", entry.getKey()); // Month is a String
                    monthRevenue.put("revenue", entry.getValue()); // Revenue is a Double
                    return monthRevenue;
                })
                .collect(Collectors.toList()); // Collect into a list
    }






}
