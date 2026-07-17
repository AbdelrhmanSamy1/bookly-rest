package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    @Schema(description = "ID of the book to order", example = "1")
    @NotNull
    private Long bookId;

    @Schema(description = "Quantity of the book to order", example = "2")
    @Min(1)
    private Integer quantity;
}
