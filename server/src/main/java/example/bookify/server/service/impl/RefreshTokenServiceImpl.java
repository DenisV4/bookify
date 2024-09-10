package example.bookify.server.service.impl;

import lombok.RequiredArgsConstructor;
import example.bookify.server.exception.RefreshTokenException;
import example.bookify.server.model.RefreshToken;
import example.bookify.server.repository.RefreshTokenRepository;
import example.bookify.server.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository tokenRepository;

    @Value("${app.security.jwt.refreshTokenExpiration}")
    private Duration accessTokenExpiration;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        var refreshToken = RefreshToken
                .builder()
                .userId(userId)
                .expirationTime(LocalDateTime.now().plusSeconds(accessTokenExpiration.toSeconds()))
                .token(UUID.randomUUID().toString())
                .build();

        refreshToken = tokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Refresh token has expired.");
        }

        return token;
    }

    public void deleteByUserId(Long userId) {
        tokenRepository.findByUserId(userId).ifPresent(tokenRepository::delete);
    }
}
