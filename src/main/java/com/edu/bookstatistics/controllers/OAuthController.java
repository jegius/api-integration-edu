package com.edu.bookstatistics.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/oauth")
@Tag(name = "OAuth Controller", description = "API для авторизации через OAuth2")
public class OAuthController {

    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);

    @GetMapping("/login")
    @Operation(
            summary = "Перенаправление на страницу авторизации",
            description = "Перенаправляет пользователя на страницу авторизации выбранного OAuth2 провайдера (например, Google)."
    )
    public String login() {
        return "redirect:/oauth2/authorization/google";
    }

    @ResponseBody
    @GetMapping("/loginSuccess")
    @Operation(
            summary = "Получить информацию о вошедшем пользователе или token",
            description = "Возвращает информацию об аутентифицированном пользователе и токене после успешного входа через OAuth2.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена")
            }
    )
    public Map<String, Object> getLoginInfo(@AuthenticationPrincipal OidcUser oidcUser) {
        Map<String, Object> userInfo = new HashMap<>();
        if (oidcUser != null) {
            String googleId = oidcUser.getSubject();
            String name = oidcUser.getFullName();
            String email = oidcUser.getEmail();
            String picture = oidcUser.getPicture();

            userInfo.put("id", googleId);
            userInfo.put("name", name);
            userInfo.put("email", email);
            userInfo.put("picture", picture);
        }
        return userInfo;
    }
}