package org.example.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class JwtUtil {

    public static String resolveAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String bearerToken = "";
        if (cookies != null) {
            bearerToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(JwtFilter.AUTHORIZATION_HEADER))
                    .map(Cookie::getValue)
                    .findFirst().orElse("");
            bearerToken = URLDecoder.decode(bearerToken, StandardCharsets.UTF_8);
        }

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

}
