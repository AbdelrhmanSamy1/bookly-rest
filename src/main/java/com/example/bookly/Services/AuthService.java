package com.example.bookly.Services;

import com.example.bookly.dto.request.LoginRequestDto;
import com.example.bookly.dto.request.RegisterRequestDto;
import com.example.bookly.dto.response.AuthResponseDto;
import com.example.bookly.entity.RefreshToken;
import com.example.bookly.entity.User;
import com.example.bookly.repository.UserRepository;
import com.example.bookly.security.JwtAuthFilter;
import com.example.bookly.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public AuthResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UsernameNotFoundException("Email already in use: " + dto.getEmail());
        }
        User user = User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(User.Role.USER)
                .build();
        userRepository.save(user);

        return buildAuthResponse(user.getEmail());


    }
    public AuthResponseDto login(LoginRequestDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        return buildAuthResponse(dto.getEmail());
    }
    public AuthResponseDto refreshToken(String refreshTokenValue) {
        RefreshToken rotated = refreshTokenService.rotateRefreshToken(refreshTokenValue);
        User user = rotated.getUser();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String newAccessToken = jwtService.generateToken(userDetails);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(rotated.getToken())
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public void logout(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        refreshTokenService.deleteByUser(user);
    }
    private AuthResponseDto buildAuthResponse(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        User user = userRepository.findByEmail(email).orElseThrow();

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refresh = refreshTokenService.createRefreshToken(email);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refresh.getToken())
                .email(user.getEmail())
                .role(user.getRole().name())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
