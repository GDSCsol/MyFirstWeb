package org.example.security.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.security.dto.LoginDto;
import org.example.security.dto.UserDto;
import org.example.security.jwt.JwtFilter;
import org.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@AllArgsConstructor
@RequestMapping("/jwt")
public class JwtTemplateController {

    private final UserService userService;

    @GetMapping
    public String index() {return "jwt/jwt";}

    @GetMapping("/login")
    public String login(LoginDto loginDto) {
        return "jwt/login";
    }

    @PostMapping("/login")
    public String authorize(@Valid LoginDto loginDto,
                            BindingResult bindingResult,
                            @Value("${jwt.token-validity-in-seconds}") int tokenValidityInSeconds,
                            HttpServletResponse response) {
        try {
            // 쿠키저장
            String jwt = userService.login(loginDto);
            String cookieValue = URLEncoder.encode("Bearer " + jwt, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, cookieValue);
            cookie.setMaxAge(tokenValidityInSeconds);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";

        } catch (Exception e) {
            ObjectError error = new ObjectError("loginDto", e.getMessage());
            bindingResult.addError(error);
        }

        return "jwt/login";
    }

    @GetMapping("/signup")
    public String signup(UserDto userDto) {
        return "jwt/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserDto userDto,
                         BindingResult bindingResult) {
        try {
            userService.signup(userDto);
            return "redirect:/";
        } catch (Exception e) {
            ObjectError error = new ObjectError("userDto", e.getMessage());
            bindingResult.addError(error);
        }

        return "jwt/signup";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 제거
        Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}