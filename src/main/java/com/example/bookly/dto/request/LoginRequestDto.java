package com.example.bookly.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank @Email
    private String email;

    @Schema(description = "User's password", example = "securePassword123")
    @NotBlank
    private String password;
}
