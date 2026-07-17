package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookRequestDto {
    @Schema(description = "Title of the book", example = "A Game of Thrones")
    @NotBlank
    @Size(min = 1, max = 200)
    private String title;

    @Schema(description = "ISBN number of the book", example = "978-0553103540")
    @NotBlank
    private String isbn;

    @Schema(description = "Description or synopsis of the book", example = "The first novel in A Song of Ice and Fire.")
    @Size(max = 2000)
    private String description;

    @Schema(description = "Price of the book", example = "29.99")
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @Schema(description = "Number of items currently in stock", example = "100")
    @Min(0)
    private Integer stockQuantity;

    @Schema(description = "Year the book was published", example = "1996")
    @Min(1000) @Max(9999)
    private Integer publishedYear;

    @Schema(description = "ID of the author", example = "1")
    @NotNull
    private Long authorId;

    @Schema(description = "ID of the category", example = "1")
    @NotNull
    private Long categoryId;
}
