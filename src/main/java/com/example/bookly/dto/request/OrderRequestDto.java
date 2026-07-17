package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    @Schema(description = "ID of the user placing the order", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "List of items in the order")
    @NotEmpty
    private List<OrderItemRequestDto> items;

}
