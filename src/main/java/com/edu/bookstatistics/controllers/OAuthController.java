package com.edu.bookstatistics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@Tag(name = "OAuth Controller", description = "API для авторизации через OAuth2")
public class OAuthController {

    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);
    private final JwtEncoder jwtEncoder;

    public OAuthController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @GetMapping("/login")
    @Operation(
            summary = "Перенаправление на страницу авторизации",
            description = "Перенаправляет пользователя на страницу авторизации выбранного OAuth2 провайдера (например, Google)."
    )
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/loginSuccess")
    @Operation(
            summary = "Получить информацию о вошедшем пользователе или token",
            description = "Возвращает JWT токен после успешного входа через OAuth2.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "JWT токен успешно получен")
            }
    )
    public Map<String, String> getLoginInfo(@AuthenticationPrincipal OidcUser oidcUser, Authentication authentication) {
        if (oidcUser == null) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }

        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(oidcUser.getSubject())
                .claim("name", oidcUser.getFullName())
                .claim("email", oidcUser.getEmail())
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}