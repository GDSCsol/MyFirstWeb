package org.example.security.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.security.dto.LoginDto;
import org.example.security.dto.TokenDto;
import org.example.security.dto.UserDto;
import org.example.security.jwt.JwtFilter;
import org.example.security.jwt.TokenProvider;
import org.example.security.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@AllArgsConstructor
public class AuthTemplateController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(LoginDto loginDto) {
        return "login";
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
            response.addCookie(cookie);
            return "redirect:/";

        } catch (Exception e) {
            ObjectError error = new ObjectError("loginDto", e.getMessage());
            bindingResult.addError(error);
        }

        return "login";
    }

    @GetMapping("/signup")
    public String signup(UserDto userDto) {
        return "signup";
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

        return "signup";
    }

}
