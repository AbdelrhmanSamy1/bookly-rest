package com.example.bookly.controllers;

import com.example.bookly.Services.OrderService;
import com.example.bookly.dto.request.OrderRequestDto;
import com.example.bookly.dto.response.OrderResponseDto;
import com.example.bookly.entity.Order;
import com.example.bookly.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Place and manage customer orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "List all orders (ADMIN)",
               description = "Returns all orders in the system. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @Operation(summary = "Get an order by ID",
               description = "Returns a single order with its items. Eagerly fetches order items and book details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getById(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    @Operation(summary = "Get orders by user ID",
               description = "Returns all orders placed by a specific user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User orders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getByUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(orderService.findByUserId(userId));
    }

    @Operation(summary = "Place a new order",
               description = "Creates a new order for a user. Each item must reference an existing book ID and a quantity. "
                       + "Stock is automatically decremented. Fails if any book has insufficient stock.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order placed successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or insufficient stock",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or Book not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@Valid @RequestBody OrderRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(dto));
    }

    @Operation(summary = "Update order status (ADMIN)",
               description = "Changes the status of an existing order. Requires ADMIN role. "
                       + "Cancelling an order automatically restores stock quantities.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateStatus(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "New order status", example = "CONFIRMED",
                    schema = @Schema(implementation = Order.OrderStatus.class))
            @RequestParam Order.OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @Operation(summary = "Delete an order (ADMIN)",
               description = "Permanently removes an order. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Order ID", example = "1")
            @PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}