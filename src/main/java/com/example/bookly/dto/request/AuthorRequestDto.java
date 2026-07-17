package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthorRequestDto {
    @Schema(description = "Full name of the author", example = "George R. R. Martin")
    @NotBlank(message = "Author name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Schema(description = "Short biography of the author", example = "American novelist and short story writer, best known for A Song of Ice and Fire.")
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    @Schema(description = "Nationality of the author", example = "American")
    private String nationality;
}
