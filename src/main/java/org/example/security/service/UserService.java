package org.example.security.service;

import lombok.AllArgsConstructor;
import org.example.security.dto.AccessRefreshTokenDto;
import org.example.security.dto.LoginDto;
import org.example.security.dto.UserDto;
import org.example.security.entity.Authority;
import org.example.security.entity.AuthorityEnum;
import org.example.security.entity.User;
import org.example.security.entity.UserAuthority;
import org.example.security.exception.DuplicateMemberException;
import org.example.security.exception.NotFoundMemberException;
import org.example.security.jwt.TokenProvider;
import org.example.security.repository.RefreshTokenRepository;
import org.example.security.repository.UserAuthorityRepository;
import org.example.security.repository.UserRepository;
import org.example.security.util.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AccessRefreshTokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createAccessToken(authentication);
        String rft = tokenProvider.createRefreshtoken(authentication);

        if (refreshTokenRepository.findByAccessToken(jwt).isPresent()) {
            refreshTokenService.removeRefreshToken(jwt);
        }
        refreshTokenService.saveTokenInfo(loginDto.getName(), jwt, rft);

        return new AccessRefreshTokenDto(jwt, rft);
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByName(userDto.getName()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityEnum(AuthorityEnum.ROLE_USER)
                .build();

        User user = User.builder()
                .name(userDto.getName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(new HashSet<>())
                .activated(true)
                .build();

        UserAuthority userAuthority = UserAuthority.builder()
                .user(user)
                .authority(authority)
                .build();

        user.addUserAuthority(userAuthority);

        userAuthorityRepository.save(userAuthority);
        userRepository.save(user);
        return UserDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByName(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByName)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}