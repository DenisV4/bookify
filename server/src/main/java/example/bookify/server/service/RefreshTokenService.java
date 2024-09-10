package example.bookify.server.service;

import example.bookify.server.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {

    public Optional<RefreshToken> findByToken(String token);

    public RefreshToken createRefreshToken(Long userId);

    public RefreshToken checkRefreshToken(RefreshToken token);

    public void deleteByUserId(Long userId);
}
