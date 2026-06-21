package com.example.bookly.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    @NotNull
    private Long bookId;

    @Min(1)
    private Integer quantity;
}
