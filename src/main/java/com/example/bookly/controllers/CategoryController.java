package com.example.bookly.controllers;

import com.example.bookly.Services.CategoryService;
import com.example.bookly.dto.request.CategoryRequestDto;
import com.example.bookly.dto.response.CategoryResponseDto;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Manage book categories — CRUD operations")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "List all categories",
               description = "Returns all book categories. No authentication required.")
    @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Get a category by ID",
               description = "Returns a single category by its unique identifier. No authentication required.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getById(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(summary = "Create a new category",
               description = "Adds a new book category. Requires ADMIN role. Category names must be unique (case-insensitive).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Category with this name already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(dto));
    }

    @Operation(summary = "Update a category",
               description = "Updates an existing category's details. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> update(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable long id,
            @Valid @RequestBody CategoryRequestDto dto) {
        return ResponseEntity.ok(categoryService.update(dto, id));
    }

    @Operation(summary = "Delete a category",
               description = "Permanently removes a category. Requires ADMIN role. "
                       + "Will also cascade-delete all books in this category.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Category ID", example = "1")
            @PathVariable long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
