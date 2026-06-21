package com.example.bookly.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Integer publishedYear;
    private String authorName;      // flattened — no nested Author object
    private String categoryName;

}
