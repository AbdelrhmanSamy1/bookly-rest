package com.example.bookly.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {
    @NotNull
    private Long userId;

    @NotEmpty
    private List<OrderItemRequestDto> items;

}
