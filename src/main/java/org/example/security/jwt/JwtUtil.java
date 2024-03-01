package org.example.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

public class JwtUtil {

    public static String resolveToken(HttpServletRequest request, String tokenHeader) {
        Cookie[] cookies = request.getCookies();
        String bearerToken = "";
        if (cookies != null) {
            bearerToken = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(tokenHeader))
                    .map(Cookie::getValue)
                    .findFirst().orElse("");
            bearerToken = URLDecoder.decode(bearerToken, StandardCharsets.UTF_8);
        }

        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith("Bearer ")) { return bearerToken.substring(7);}
            return bearerToken;
        }

        return null;
    }

    public static String resolveAccessToken(HttpServletRequest request) {
        return resolveToken(request, JwtFilter.AUTHORIZATION_HEADER);
    }

    public static String resolveRefreshToken(HttpServletRequest request) {
        return resolveToken(request, JwtFilter.REFRESH_TOKEN_HEADER);
    }

}
