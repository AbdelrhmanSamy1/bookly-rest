package com.example.bookly.controllers;

import com.example.bookly.Services.UserService;
import com.example.bookly.dto.request.UserRequestDto;
import com.example.bookly.dto.response.UserResponseDto;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management — mostly admin-only operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "List all users (ADMIN)",
               description = "Returns all registered users. Requires ADMIN role.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Get a user by ID",
               description = "Returns a single user by their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "Create a new user",
               description = "Creates a new user account with the USER role. Email must be unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @Operation(summary = "Update a user",
               description = "Updates an existing user's first name, last name, and email. "
                       + "Password and role cannot be changed through this endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @Operation(summary = "Delete a user (ADMIN)",
               description = "Permanently removes a user and all associated data (orders, refresh tokens). Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}