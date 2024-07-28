package com.edu.bookstatistics.config;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {

    private final JwtDecoder jwtDecoder;

    public JwtTokenValidator(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public Jwt validateToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException ex) {
            throw new RuntimeException("Invalid JWT token", ex);
        }
    }
}