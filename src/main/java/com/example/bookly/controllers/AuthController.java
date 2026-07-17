package com.example.bookly.controllers;

import com.example.bookly.Services.AuthService;
import com.example.bookly.dto.RefreshTokenRequestDto;
import com.example.bookly.dto.request.LoginRequestDto;
import com.example.bookly.dto.request.RegisterRequestDto;
import com.example.bookly.dto.response.AuthResponseDto;
import com.example.bookly.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, login, refresh tokens, and logout")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user",
               description = "Creates a new user account. The first registered user is automatically assigned the ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error — invalid input fields",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @Operation(summary = "Login",
               description = "Authenticates a user with email and password. Returns JWT access token and refresh token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(summary = "Refresh access token",
               description = "Exchanges a valid refresh token for a new access token and a rotated refresh token. "
                       + "Each refresh token can only be used once (rotation). Reuse is detected and triggers revocation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens refreshed successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token expired, already used, or not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @RequestBody RefreshTokenRequestDto dto){
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }

    @Operation(summary = "Logout",
               description = "Revokes all refresh tokens for the authenticated user. Requires a valid JWT access token.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserDetails userDetails){
        authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
