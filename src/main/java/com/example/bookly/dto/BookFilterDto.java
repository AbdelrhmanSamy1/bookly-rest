package com.example.bookly.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookFilterDto {
    private String title;
    private String authorName;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer publishedYear;
    private Boolean inStock;
}
