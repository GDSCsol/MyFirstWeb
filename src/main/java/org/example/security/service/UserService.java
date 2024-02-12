package org.example.security.service;

import io.jsonwebtoken.lang.Collections;
import org.example.security.dto.UserDto;
import org.example.security.entity.Authority;
import org.example.security.entity.AuthorityEnum;
import org.example.security.entity.User;
import org.example.security.entity.UserAuthority;
import org.example.security.exception.DuplicateMemberException;
import org.example.security.exception.NotFoundMemberException;
import org.example.security.repository.UserAuthorityRepository;
import org.example.security.repository.UserRepository;
import org.example.security.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserAuthorityRepository userAuthorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userAuthorityRepository = userAuthorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByName(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityEnum(AuthorityEnum.ROLE_USER)
                .build();

        User user = User.builder()
                .name(userDto.getUsername())
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