package com.example.bookly.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequestDto {
    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @NotBlank
    private String isbn;

    @Size(max = 2000)
    private String description;


    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Min(0)
    private Integer stockQuantity;

    @Min(1000) @Max(9999)
    private Integer publishedYear;

    @NotNull
    private Long authorId;

    @NotNull
    private Long categoryId;
}
