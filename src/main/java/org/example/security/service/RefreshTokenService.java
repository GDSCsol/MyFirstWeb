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
    public void saveTokenInfo(String name, String refreshToken, String accressToken) {
        refreshTokenRepository.save(new RefreshToken(name, accressToken, refreshToken));
    }

    @Transactional
    // TODO: 2024-02-27 username으로 지우는 방안 생각해보기 
    public void removeRefreshToken(String accessToken) {
        RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalAccessError::new);
        
        refreshTokenRepository.delete(token);
    }
}
