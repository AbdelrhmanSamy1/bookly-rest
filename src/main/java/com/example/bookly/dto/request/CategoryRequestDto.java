package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @Schema(description = "Name of the category", example = "Fantasy")
    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Name must not exceed 50 characters")
    private String name;

    @Schema(description = "Description of the category", example = "Books containing magical or supernatural elements.")
    @Size(max = 255)
    private String description;
}
