[< Назад к README.md](../../../../README.md)

# SecurityConfig

## Навигация

- [Описание](#описание)
- [Общая концепция](#общая-концепция)
- [Состав и работа кода](#состав-и-работа-кода)
  - [Импорты](#импорты)
  - [Аннотации](#аннотации)
  - [Поля и Конструктор](#поля-и-конструктор)
  - [Метод `securityFilterChain`](#метод-securityfilterchain)
  - [Методы для CORS и Обработчика успешной аутентификации](#методы-для-cors-и-обработчика-успешной-аутентификации)
  - [Методы для Настройки JWT](#методы-для-настройки-jwt)
  - [Генерация Ключевой Пары](#генерация-ключевой-пары)

- [Описание](#описание)
    - [Аннотация `@Configuration`](#аннотация-configuration)
    - [Метод `customOpenAPI`](#метод-customopenapi)
- [Как это работает?](#как-это-работает)
- [Использование многопоточности в проекте](#использование-многопоточности-в-проекте)
- [Дополнительные ресурсы](#дополнительные-ресурсы)

- [Описание](#описание)
- [Общая концепция](#общая-концепция)
- [Состав и работа кода](#состав-и-работа-кода)
    - [Импорты](#импорты-1)
    - [Определение класса](#определение-класса)
    - [Метод `onAuthenticationSuccess`](#метод-onauthenticationsuccess)


## Описание

Этот файл README описывает работу и компоненты класса `SecurityConfig`, который является конфигурационным классом безопасности в проекте Spring Boot. Этот класс отвечает за настройку аутентификации и авторизации с использованием OAuth2, JWT (JSON Web Token), а также настройку CORS и других параметров безопасности.

## Общая концепция

Класс `SecurityConfig` настраивает:

1. Авторизация HTTP запросов.
2. Вход через OAuth2 с пользовательским обработчиком успешной аутентификации.
3. Ресурсный сервер OAuth2 для декодирования JWT.
4. Обработка исключений для неавторизованных запросов.
5. Управление сессиями.
6. Настройка CORS (Cross-Origin Resource Sharing).
7. Создание ключевой пары для генерации и верификации JWT.

## Состав и работа кода

### Импорты

```java
import com.edu.bookstatistics.services.CustomOidcUserService;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;
```

### Аннотации

- `@Configuration`: Обозначает, что данный класс является конфигурационным и содержит определения бинов (bean).
- `@EnableWebSecurity`: Включает интеграцию безопасности с Spring Security.

### Поля и Конструктор

```java
public class SecurityConfig {

    private final CustomOidcUserService customOidcUserService;
    private final KeyPair keyPair;

    public SecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
        this.keyPair = generateRsaKey();
    }
```

- `customOidcUserService`: Сервис, обрабатывающий пользовательские детали в ходе аутентификации через OIDC.
- `keyPair`: Генерируемая пара ключей RSA для использования в JWT.

### Метод `securityFilterChain`

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                    .requestMatchers("/api/oauth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/api/books/**", "/api/progress/**", "/api/users/**").authenticated()
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                    .successHandler(customOAuth2SuccessHandler())
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.oidcUserService(customOidcUserService))
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())))
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

    return http.build();
}
```

- Настройка авторизации запросов:
    - Некоторые URL разрешены всем (`permitAll()`).
    - Другие требуют аутентификации (`authenticated()`).
- Настройка входа через OAuth2:
    - Использование пользовательского обработчика успешной аутентификации.
    - Настройка службы для получения информации о пользователях.
- Настройка ресурсного сервера для декодирования JWT.
- Обработка исключений для неавторизованных запросов.
- Настройка управления сессиями в состоянии `STATELESS`.
- Отключение CSRF-защиты для REST API.
- Настройка CORS.

### Методы для CORS и Обработчика успешной аутентификации

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:8080"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}

@Bean
public AuthenticationSuccessHandler customOAuth2SuccessHandler() {
    return new CustomOAuth2SuccessHandler(jwtEncoder());
}
```

- `corsConfigurationSource`: Настраивает допустимые источники, методы и заголовки для CORS.
- `customOAuth2SuccessHandler`: Возвращает пользовательский обработчик успешной аутентификации.

### Методы для Настройки JWT

```java
@Bean
public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey((RSAPublicKey) keyPair.getPublic()).build();
}

@Bean
public JwtEncoder jwtEncoder() {
    RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
            .privateKey(keyPair.getPrivate())
            .build();
    JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(rsaKey));
    return new NimbusJwtEncoder(jwkSource);
}
```

- `jwtDecoder`: Настраивает декодер JWT с использованием публичного ключа RSA.
- `jwtEncoder`: Настраивает энкодер JWT с использованием пары ключей RSA.

### Генерация Ключевой Пары

```java
private KeyPair generateRsaKey() {
    try {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    }
}
```

- Метод для генерации пары ключей RSA с размером ключа 2048 бит.

# CustomOAuth2SuccessHandler

## Описание

Этот файл описывает работу и компоненты класса `CustomOAuth2SuccessHandler`, который является пользовательским обработчиком успешной аутентификации для OAuth2 в Spring Security. В данном классе происходит генерация JWT (JSON Web Token) после успешной аутентификации пользователя с использованием OAuth2.

## Общая концепция

Когда пользователь успешно проходит аутентификацию через OAuth2, Spring Security передает управление нашему `CustomOAuth2SuccessHandler`. В этом обработчике создается и возвращается JWT, который содержит информацию о пользователе, срок действия и другие атрибуты.

## Состав и работа кода

### Импорты

```java
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
```

Здесь импортируются необходимые классы для работы с аутентификацией и JWT, а также классы для работы с HTTP-запросами и ответами и обработки времени.

### Определение класса

```java
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtEncoder jwtEncoder;

    public CustomOAuth2SuccessHandler(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // Метод будет описан далее
    }
}
```

- `CustomOAuth2SuccessHandler` реализует интерфейс `AuthenticationSuccessHandler`.
- В класс передается объект `JwtEncoder`, который используется для создания JWT.

### Метод `onAuthenticationSuccess`

```java
@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
    OidcUser oidcUser = (OidcUser) authenticationToken.getPrincipal();
    
    Instant now = Instant.now();
    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
            .subject(oidcUser.getSubject())
            .claim("name", oidcUser.getFullName())
            .claim("email", oidcUser.getEmail())
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .build();
    
    String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"token\":\"" + token + "\"}");
}
```
1. Преобразование объекта `Authentication` в `OAuth2AuthenticationToken`:
    ```java
    OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
    ```
2. Получение пользователя `OidcUser` из токена:
    ```java
    OidcUser oidcUser = (OidcUser) authenticationToken.getPrincipal();
    ```
3. Создание объекта `Instant` для задания текущего времени:
    ```java
    Instant now = Instant.now();
    ```
4. Построение набора утверждений `JwtClaimsSet` для JWT:
    ```java
    JwtClaimsSet claimsSet = JwtClaimsSet.builder()
            .subject(oidcUser.getSubject())
            .claim("name", oidcUser.getFullName())
            .claim("email", oidcUser.getEmail())
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .build();
    ```
    - Задание субъекта (`subject`), который представляет идентификатор пользователя.
    - Включение в токен дополнительных утверждений: имя (`name`) и электронная почта (`email`).
    - Указание времени создания токена и его срока действия (1 час).

5. Кодирование JWT с использованием `JwtEncoder`:
    ```java
    String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    ```
6. Настройка и отправка HTTP-ответа с JWT:
    ```java
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write("{\"token\":\"" + token + "\"}");
    ```

# OpenApiConfig

## Описание

Этот проект включает в себя конфигурацию и контроллер для API, который позволяет конвертировать изображения в различные форматы, а также реализует как однопоточную, так и многопоточную обработку изображений. В этом README файле мы сосредоточимся на классе `OpenApiConfig`, который отвечает за настройку документации Swagger для вашего API.

### Аннотация `@Configuration`

Аннотация `@Configuration` указывает на то, что класс `OpenApiConfig` является конфигурационным классом Spring. Это значит, что Spring будет обрабатывать его как источник определений бинов и других конфигурационных мета-данных.

### Метод `customOpenAPI`

Метод `customOpenAPI` помечен аннотацией `@Bean`. Это значит, что он будет доступен как бин в контексте Spring. Метод создает и возвращает экземпляр `OpenAPI`, который используется для настройки документации API.

- `info`: Метод `info` добавляет основную информацию о вашем API, такую как заголовок, описание и версию.
- `components`: Метод `components` добавляет дополнительные схемы, которые могут быть использованы в вашем API. В данном случае, добавляется схема для массива файлов `MultipartFileArray`, которые будут загружаться в бинарном формате.

```java
package com.edu.bookstatistics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Image Conversion API")
                        .description("API for converting images to different formats")
                        .version("1.0"))
                .components(new Components().addSchemas("MultipartFileArray", new Schema()
                        .type("array")
                        .items(new Schema().type("string").format("binary"))));
    }
}
```

## Как это работает?

- **Конфигурация**: Когда приложение Spring Boot запускается, оно сканирует классы с аннотацией `@Configuration` и создает бины для методов, помеченных аннотацией `@Bean`.
- **Документация**: С помощью OpenAPI и Swagger UI можно сгенерировать и визуализировать документацию для вашего API, которая будет включать описания всех доступных методов, их параметры и ожидаемые ответы.
- **Схемы**: Добавление схем (таких как `MultipartFileArray`) помогает более точно документировать сложные структуры данных, используемые в вашем API.

## Использование многопоточности в проекте

Многопоточность используется для повышения производительности при обработке нескольких изображений параллельно. В проекте есть два метода для конвертации изображений:

1. **Однопоточная обработка**: Все изображения обрабатываются последовательно в одном потоке.
2. **Многопоточная обработка**: Изображения обрабатываются параллельно, что увеличивает скорость обработки при наличии большого количества изображений.

Два маршрута API в `ImageProcessingController` реализуют эти методы, и в логах записывается время, затраченное на выполнение каждой из операций, что позволяет сравнительно оценить производительность однопоточной и многопоточной обработки.

## Дополнительные ресурсы

Если вы только начинаете изучать Java и многопоточность, рекомендуем ознакомиться с основными концепциями:

- **Потоки (Threads)**: Поток — это единица выполнения в программе. Java предоставляет классы `Thread` и интерфейсы `Runnable` для реализации многопоточности.
- **ExecutorService**: Утилита из библиотеки `java.util.concurrent`, упрощающая управление пулами потоков.
- **Future и Callable**: Более продвинутые интерфейсы для выполнения асинхронных задач с возможностью получения результата или обработки исключений.

Эти знания помогут вам лучше понять, как и зачем применяется многопоточность в реальных приложениях, таких как этот проект.