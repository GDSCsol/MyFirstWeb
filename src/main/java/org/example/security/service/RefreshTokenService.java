package org.example.security.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.exception.CustomException;
import org.example.exception.TokenErrorCode;
import org.example.exception.UserErrorCode;
import org.example.security.dto.AccessRefreshTokenDto;
import org.example.security.dto.UserDto;
import org.example.security.entity.RefreshToken;
import org.example.security.entity.User;
import org.example.security.jwt.JwtUtil;
import org.example.security.jwt.TokenProvider;
import org.example.security.repository.RefreshTokenRepository;
import org.example.security.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void saveTokenInfo(String name, String accessToken, String refreshToken) {
        refreshTokenRepository.save(new RefreshToken(name, accessToken, refreshToken));
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        RefreshToken token = refreshTokenRepository.findByAccessToken(accessToken)
                .orElseThrow(IllegalAccessError::new);

        refreshTokenRepository.delete(token);
    }

    @Transactional
    public AccessRefreshTokenDto getNewAccessToken(HttpServletRequest request) {
        String jwt = JwtUtil.resolveAccessToken(request);
        String rft = JwtUtil.resolveRefreshToken(request);
        RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(jwt)
                .orElseThrow(() -> new CustomException(TokenErrorCode.INVALID_ACCESSTOKEN));

        if (tokenProvider.expiredToken(jwt) && refreshToken.getRefreshToken().equals(rft)) {
            this.removeRefreshToken(jwt);

            User user = userRepository.findOneWithAuthoritiesByName(refreshToken.getName())
                    .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

            List<GrantedAuthority> authorities = user.getAuthorities().stream()
                    .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getAuthority().getAuthorityEnum().name()))
                    .collect(Collectors.toList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getName(), "", authorities);

            String newJwt = tokenProvider.createAccessToken(authentication);
            String newRft = tokenProvider.createRefreshtoken(authentication);

            this.saveTokenInfo(user.getName(), newJwt, newRft);

            return new AccessRefreshTokenDto(newJwt, newRft);
        } else {
            throw new CustomException(TokenErrorCode.INVALID_REFRESHTOKEN);
        }
    }
}
