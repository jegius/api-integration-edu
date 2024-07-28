package com.edu.bookstatistics.config;

import com.edu.bookstatistics.services.CustomOidcUserService;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    private final CustomOidcUserService customOidcUserService;

    public SecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/oauth/**").permitAll()
                                .requestMatchers("/api/books/**", "/api/progress/**", "/api/users/**").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 ->
                        oauth2
                                .successHandler(customOAuth2SuccessHandler())
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.oidcUserService(customOidcUserService)
                                )
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.decoder(jwtDecoder())
                        )
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customOAuth2SuccessHandler() {
        return new CustomOAuth2SuccessHandler(jwtEncoder());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        KeyPair keyPair = generateRsaKey();

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .build();

        JWKSource<SecurityContext> jwkSource = (jwkSelector, context) -> jwkSelector.select(new JWKSet(rsaKey));

        return new NimbusJwtEncoder(jwkSource);
    }

    private KeyPair generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}