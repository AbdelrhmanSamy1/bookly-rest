package com.example.bookly.controllers;

import com.example.bookly.Services.AuthService;
import com.example.bookly.dto.RefreshTokenRequestDto;
import com.example.bookly.dto.request.LoginRequestDto;
import com.example.bookly.dto.request.RegisterRequestDto;
import com.example.bookly.dto.response.AuthResponseDto;
import com.example.bookly.entity.User;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @RequestBody RefreshTokenRequestDto dto){
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserDetails userDetails){
        authService.logout(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
