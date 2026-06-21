package com.example.bookly.mapper;

import com.example.bookly.dto.response.OrderItemResponseDto;
import com.example.bookly.dto.response.OrderResponseDto;
import com.example.bookly.entity.Order;
import com.example.bookly.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "book.title", target = "bookTitle")
    OrderItemResponseDto toItemDto(OrderItem order);

    @Named("statusToString")
    default String statusToString(Order.OrderStatus status) {
        return status != null ? status.name() : null;
    }
}
