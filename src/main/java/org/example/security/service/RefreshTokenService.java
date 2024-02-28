package org.example.security.service;

import lombok.RequiredArgsConstructor;
import org.example.security.entity.RefreshToken;
import org.example.security.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveTokenInfo(String name, String accessToken, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(name, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalAccessError::new);

    }
}
