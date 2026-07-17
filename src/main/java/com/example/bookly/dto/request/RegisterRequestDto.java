package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {
    @Schema(description = "User's first name", example = "John")
    @NotBlank @Size(min = 2, max = 50)
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @NotBlank @Size(min = 2, max = 50)
    private String lastName;

    @Schema(description = "User's email address (must be unique)", example = "john.doe@example.com")
    @NotBlank @Email
    private String email;

    @Schema(description = "User's chosen password (min 8 characters)", example = "securePassword123")
    @NotBlank
    @Size(min = 8)
    private String password;
}
