package com.example.bookly.Services;

import com.example.bookly.entity.RefreshToken;
import com.example.bookly.entity.User;
import com.example.bookly.exception.InvalidTokenException;
import com.example.bookly.repository.RefreshTokenRepository;
import com.example.bookly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException("User not found with email: " + email));

        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plusMillis(refreshExpiration))
                .used(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken rotateRefreshToken(String oldTokenValue) {
        RefreshToken oldToken = refreshTokenRepository.findByToken(oldTokenValue)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found — please login again"));

        if (oldToken.isUsed()) {
            refreshTokenRepository.deleteByUser(oldToken.getUser());
            throw new InvalidTokenException("Refresh token already used — please login again");
        }
        if (oldToken.getExpiresAt().isBefore(Instant.now())) {
            refreshTokenRepository.delete(oldToken);
            throw new InvalidTokenException("Refresh token expired — please login again");
        }

        RefreshToken newToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(oldToken.getUser())
                .expiresAt(Instant.now().plusMillis(refreshExpiration))
                .used(false)
                .build();

        refreshTokenRepository.save(newToken);
        refreshTokenRepository.delete(oldToken);
        return newToken;
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
