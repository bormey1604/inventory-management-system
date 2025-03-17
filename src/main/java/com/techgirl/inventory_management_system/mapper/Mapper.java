package com.techgirl.inventory_management_system.mapper;

import com.techgirl.inventory_management_system.dto.CategoryDto;
import com.techgirl.inventory_management_system.dto.ProductDto;
import com.techgirl.inventory_management_system.dto.SaleRequestDto;
import com.techgirl.inventory_management_system.models.Category;
import com.techgirl.inventory_management_system.models.Product;
import com.techgirl.inventory_management_system.models.Sale;
import com.techgirl.inventory_management_system.models.SaleItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    private Mapper() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }


    public static Product toProduct(ProductDto dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getStock(),
                dto.getCategoryId()
        );
    }

    public static ProductDto fromProduct(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock(),
                product.getCategoryId()
        );
    }

    public static Category toCategory(CategoryDto dto) {
        List<Product> products = dto.getProducts() != null
                ? dto.getProducts().stream().map(Mapper::toProduct).toList()
                : new ArrayList<>();

        return new Category(
                dto.getId(),
                dto.getName(),
                products
        );
    }

    public static CategoryDto fromCategory(Category category) {
        List<ProductDto> products = category.getProducts() != null
                ? category.getProducts().stream().map(Mapper::fromProduct).toList()
                : new ArrayList<>();

        return new CategoryDto(
                category.getId(),
                category.getName(),
                products
        );
    }

    public static Sale toSale(SaleRequestDto saleRequestDto) {
        if (saleRequestDto == null) {
            return null;
        }

        List<SaleItem> saleItems = saleRequestDto.getSaleItems().stream()
                .map(saleItemDto -> new SaleItem(saleItemDto.getProductId(), saleItemDto.getQuantity(), saleItemDto.getPrice()))
                .collect(Collectors.toList());

        Sale sale = new Sale();
        sale.setCustomerId(saleRequestDto.getCustomerId());
        sale.setSaleItems(saleItems);
        sale.setTotalAmount(saleRequestDto.getTotalAmount());
        sale.setPaymentMethod(saleRequestDto.getPaymentMethod());

        return sale;
    }

    public static SaleRequestDto fromSale(Sale sale) {
        if (sale == null) {
            return null;
        }

        SaleRequestDto saleRequestDto = new SaleRequestDto();
        saleRequestDto.setCustomerId(sale.getCustomerId());
        saleRequestDto.setSaleItems(sale.getSaleItems());
        saleRequestDto.setTotalAmount(sale.getTotalAmount());
        saleRequestDto.setPaymentMethod(sale.getPaymentMethod());

        return saleRequestDto;
    }
}
