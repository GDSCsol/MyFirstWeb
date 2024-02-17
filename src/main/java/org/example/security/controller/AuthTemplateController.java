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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        if (bindingResult.hasErrors()) {
            return "login";
        }

        // TODO: 2024-02-17 쿠키저장안됌 
        // 쿠키저장
//        String jwt = userService.login(loginDto);
//        Cookie cookie = new Cookie(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
//        cookie.setMaxAge(tokenValidityInSeconds);
//        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserDto userDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        userService.signup(userDto);
        return "redirect:/";
    }

}
