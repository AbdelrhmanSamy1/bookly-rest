package com.example.bookly.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long id;
    private String bookTitle;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
