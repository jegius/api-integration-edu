[< Назад к README.md](../../../../README.md)

## Навигация
# Контроллер BookController
- [Описание](#описание)
- [Зависимости](#зависимости)
- [Конструктор](#конструктор)
- [Методы](#методы)
  - [Добавление книги](#добавление-книги)
  - [Обновление книги](#обновление-книги)
  - [Удаление книги](#удаление-книги)
  - [Получение всех книг](#получение-всех-книг)
  - [Получение книги по ID](#получение-книги-по-id)
- [Возврат на главный README](#возврат-на-главный-readme)

# Контроллер OAuthController
- [Описание](#описание)
- [Зависимости](#зависимости)
- [Конструктор](#конструктор)
- [Методы](#методы)
    - [Перенаправление на страницу авторизации](#перенаправление-на-страницу-авторизации)
    - [Получение информации о вошедшем пользователе или токена](#получение-информации-о-вошедшем-пользователе-или-токена)
- [Возврат на главный README](#возврат-на-главный-readme)

# Контроллер ReadingProgressController
- [Описание](#описание)
- [Зависимости](#зависимости)
- [Конструктор](#конструктор)
- [Методы](#методы)
    - [Добавить прогресс чтения](#добавить-прогресс-чтения)
    - [Получить прогресс чтения по ID книги](#получить-прогресс-чтения-по-id-книги)
- [Возврат на главный README](#возврат-на-главный-readme)

# Контроллер UserController
- [Описание](#описание)
- [Зависимости](#зависимости)
- [Конструктор](#конструктор)
- [Методы](#методы)
    - [Получение информации о текущем пользователе](#получение-информации-о-текущем-пользователе)
    - [Получение всех пользователей](#получение-всех-пользователей)
    - [Создание нового пользователя](#создание-нового-пользователя)
    - [Обновление существующего пользователя](#обновление-существующего-пользователя)
    - [Удаление пользователя](#удаление-пользователя)
- [Возврат на главный README](#возврат-на-главный-readme)

# Контроллер BookController

## Описание

Этот файл описывает работу контрреллера `BookController`, который предоставляет API для управления книгами в системе. Контроллер содержит методы для добавления, обновления, удаления и получения информации о книгах. Он предназначен для работы в безопасной среде, требующей авторизации.

## Зависимости

Контроллер `BookController` использует следующие зависимости:

- `BookService`: Сервис для бизнес-логики приложения.
- `BookDTO`: Объект передачи данных для книги.
- `Book`: Сущность книги.
- Аннотации Swagger для документирования API:
  - `@Operation`
  - `@ApiResponse`
  - `@SecurityRequirement`
  - `@Tag`
- Аннотации Spring для создания REST API и обеспечения безопасности:
  - `@RestController`
  - `@RequestMapping`
  - `@PreAuthorize`
  - `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@GetMapping`

## Конструктор

Конструктор принимает `BookService` в качестве параметра и инициализирует локальную переменную `bookService`.

```java
public BookController(BookService bookService) {
    this.bookService = bookService;
}
```

## Методы

### Добавление книги

Метод для добавления новой книги в систему.

- URL: `/api/books`
- Метод: `POST`
- Аннотация: `@PreAuthorize("isAuthenticated()")` - Требует аутентификации.
- Описание: Добавляет новую книгу, используя данные, поступающие в виде `BookDTO`.
- Пример данных запроса:
  ```json
  {
    "title": "Новое название",
    "author": "Автор",
    "totalPages": 44,
    "coverImage": "http://example.com/cover.jpg"
  }
  ```

```java
public ResponseEntity<Book> addBook(@RequestBody BookDTO bookDTO) {
    Book book = new Book();
    book.setTitle(bookDTO.getTitle());
    book.setTotalPages(bookDTO.getTotalPages());
    book.setAuthor(bookDTO.getAuthor());
    book.setCoverImage(bookDTO.getCoverImage());

    return ResponseEntity.ok(bookService.addBook(book));
}
```

### Обновление книги

Метод для обновления существующей книги.

- URL: `/api/books`
- Метод: `PUT`
- Аннотация: `@PreAuthorize("isAuthenticated()")` - Требует аутентификации.
- Описание: Обновляет данные книги, используя информацию из `BookDTO`.
- Пример данных запроса:
  ```json
  {
    "id": 1,
    "title": "Обновленное название",
    "author": "Автор",
    "totalPages": 50,
    "coverImage": "http://example.com/cover_updated.jpg"
  }
  ```

```java
public ResponseEntity<Book> updateBook(@RequestBody BookDTO bookDTO) {
    Book book = new Book();
    book.setTitle(bookDTO.getTitle());
    book.setTotalPages(bookDTO.getTotalPages());
    book.setAuthor(bookDTO.getAuthor());
    book.setCoverImage(bookDTO.getCoverImage());

    return ResponseEntity.ok(bookService.updateBook(book));
}
```

### Удаление книги

Метод для удаления книги по её ID.

- URL: `/api/books/{id}`
- Метод: `DELETE`
- Аннотация: `@PreAuthorize("isAuthenticated()")` - Требует аутентификации.
- Описание: Удаляет книгу с указанным ID.

```java
public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.ok().build();
}
```

### Получение всех книг

Метод для получения списка всех книг с пагинацией.

- URL: `/api/books`
- Метод: `GET`
- Аннотация: `@PreAuthorize("isAuthenticated()")` - Требует аутентификации.
- Описание: Возвращает список всех книг с поддержкой пагинации (по умолчанию 10 книг на страницу).

```java
public ResponseEntity<List<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(bookService.getAllBooks(page, size));
}
```

### Получение книги по ID

Метод для получения информации о книге по её ID.

- URL: `/api/books/{id}`
- Метод: `GET`
- Аннотация: `@PreAuthorize("isAuthenticated()")` - Требует аутентификации.
- Описание: Возвращает книгу с указанным ID.

```java
public ResponseEntity<Book> getBook(@PathVariable Long id) {
    return ResponseEntity.ok(bookService.getBook(id));
}
```

# Контроллер OAuthController

## Описание

Этот файл описывает работу контроллера `OAuthController`, который предоставляет API для авторизации через OAuth2. Контроллер содержит методы для перенаправления на страницу авторизации и получения информации о вошедшем пользователе, а также для генерации JWT токена после успешной авторизации.

## Зависимости

Контроллер `OAuthController` использует следующие зависимости:

- `JwtEncoder`: Компонент для кодирования JWT токенов.
- `OidcUser`: Представляет аутентифицированного пользователя OpenID Connect.
- Логгер `Logger` из `org.slf4j.LoggerFactory` для журналирования действий.
- Аннотации Swagger для документирования API:
  - `@Operation`
  - `@ApiResponse`
  - `@SecurityRequirement`
  - `@Tag`
- Аннотации Spring для создания REST API и обеспечения безопасности:
  - `@RestController`
  - `@RequestMapping`
  - `@GetMapping`
  - `@AuthenticationPrincipal`
- Классы `HttpServletRequest` и `HttpServletResponse` для работы с HTTP-запросами и ответами.

## Конструктор

Конструктор принимает `JwtEncoder` в качестве параметра и инициализирует локальную переменную `jwtEncoder`.

```java
public OAuthController(JwtEncoder jwtEncoder) {
    this.jwtEncoder = jwtEncoder;
}
```

## Методы

### Перенаправление на страницу авторизации

Метод для перенаправления пользователя на страницу авторизации выбранного OAuth2 провайдера (например, Google).

- URL: `/api/oauth/login`
- Метод: `GET`
- Аннотация: `@Operation(summary = "Перенаправление на страницу авторизации", description = "Перенаправляет пользователя на страницу авторизации выбранного OAuth2 провайдера (например, Google).")`
- Описание: Перенаправляет пользователя на страницу авторизации выбранного OAuth2 провайдера.

```java
public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.sendRedirect("/oauth2/authorization/google");
}
```

### Получение информации о вошедшем пользователе или токена

Метод для получения информации о вошедшем пользователе и генерации JWT токена после успешного входа через OAuth2.

- URL: `/api/oauth/loginSuccess`
- Метод: `GET`
- Аннотация:
    - `@Operation(summary = "Получить информацию о вошедшем пользователе или token", description = "Возвращает JWT токен после успешного входа через OAuth2.", security = @SecurityRequirement(name = "bearerAuth"), responses = { @ApiResponse(responseCode = "200", description = "JWT токен успешно получен") })`
    - `@AuthenticationPrincipal OidcUser oidcUser`: Получение информации об аутентифицированном пользователе.
- Описание: Возвращает JWT токен для аутентифицированного пользователя.

```java
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
```

# Контроллер ReadingProgressController

## Описание

Этот файл описывает работу контроллера `ReadingProgressController`, который предоставляет API для управления прогрессом чтения пользователей. Контроллер содержит методы для добавления прогресса чтения и получения прогресса по ID книги.

## Зависимости

Контроллер `ReadingProgressController` использует следующие зависимости:

- `ReadingProgressService`: Сервис для управления прогрессом чтения.
- `ReadingProgress`: Сущность, представляющая прогресс чтения.
- Логгер `Logger` из `org.slf4j.LoggerFactory` для журналирования действий (если требуется).
- Аннотации Swagger для документирования API:
  - `@Operation`
  - `@ApiResponse`
  - `@SecurityRequirement`
  - `@Tag`
- Аннотации Spring для создания REST API и обеспечения безопасности:
  - `@RestController`
  - `@RequestMapping`
  - `@PostMapping`
  - `@GetMapping`
  - `@PreAuthorize`
  - `@PathVariable`
  - `@RequestBody`
- Класс `ResponseEntity` для типизированных HTTP-ответов.

## Конструктор

Конструктор принимает `ReadingProgressService` в качестве параметра и инициализирует локальную переменную `progressService`.

```java
public ReadingProgressController(ReadingProgressService progressService) {
    this.progressService = progressService;
}
```

## Методы

### Добавить прогресс чтения

Метод для добавления прогресса чтения.

- URL: `/api/progress`
- Метод: `POST`
- Аннотация:
    - `@Operation(summary = "Добавить прогресс чтения", security = @SecurityRequirement(name = "bearerAuth"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные прогресса чтения", required = true, content = @Content(schema = @Schema(implementation = ReadingProgress.class), examples = @ExampleObject(value = "{\"date\": \"2023-10-12\", \"pagesRead\": 50, \"book\": {\"id\": 1}}") ), responses = @ApiResponse(responseCode = "200", description = "Успешное добавление прогресса"))`
    - `@PreAuthorize("isAuthenticated()")`: Аннотация для обеспечения проверки аутентификации.
- Описание: Добавляет новый прогресс чтения.

```java
public ResponseEntity<ReadingProgress> addProgress(@RequestBody ReadingProgress progress) {
    return ResponseEntity.ok(progressService.addProgress(progress));
}
```

### Получить прогресс чтения по ID книги

Метод для получения прогресса чтения по ID книги.

- URL: `/api/progress/book/{bookId}`
- Метод: `GET`
- Аннотация:
    - `@Operation(summary = "Получить прогресс чтения по ID книги", security = @SecurityRequirement(name = "bearerAuth"), responses = @ApiResponse(responseCode = "200", description = "Успешный запрос"))`
    - `@PreAuthorize("isAuthenticated()")`: Аннотация для обеспечения проверки аутентификации.
- Описание: Возвращает список прогрессов чтения для данной книги.

```java
public ResponseEntity<List<ReadingProgress>> getProgressByBookId(@PathVariable Long bookId) {
    return ResponseEntity.ok(progressService.getProgressByBookId(bookId));
}
```

# UserController

## Описание

`UserController` предоставляет REST API для управления пользователями. Это включает в себя получение информации о текущем пользователе, получение списка всех пользователей, создание нового пользователя, обновление существующего пользователя и удаление пользователя.

## Зависимости

Контроллер `UserController` использует следующие зависимости:
- `UserRepository`: Репозиторий для управления данными пользователей.
- `User`: Сущность, представляющая пользователя.
- Аннотации Swagger для документирования API:
  - `@Operation`
  - `@ApiResponse`
  - `@SecurityRequirement`
  - `@Tag`
- Аннотации Spring для создания REST API и обеспечения безопасности:
  - `@RestController`
  - `@RequestMapping`
  - `@PreAuthorize`
  - `@GetMapping`
  - `@PostMapping`
  - `@PutMapping`
  - `@DeleteMapping`
  - `@AuthenticationPrincipal`
  - `@PathVariable`
  - `@RequestBody`
- Класс `OidcUser` для аутентификации пользователей.

## Конструктор

Конструктор принимает `UserRepository` в качестве параметра и инициализирует локальную переменную `userRepository`.

```java
public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
}
```

## Методы

### Получение информации о текущем пользователе

Метод для получения информации о текущем пользователе.

- URL: `/api/users/current`
- Метод: `GET`
- Аннотации:
    - `@Operation(summary = "Get current user info", security = @SecurityRequirement(name = "bearerAuth"), responses = @ApiResponse(responseCode = "200", description = "User info retrieved"))`
    - `@PreAuthorize("isAuthenticated()")`
- Описание: Возвращает информацию о текущем пользователе, используя `OidcUser` для получения данных пользователя.

```java
public Map<String, Object> getCurrentUserInfo(@AuthenticationPrincipal OidcUser oidcUser) {
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
```

### Получение всех пользователей

Метод для получения списка всех пользователей.

- URL: `/api/users`
- Метод: `GET`
- Аннотации:
    - `@Operation(summary = "Get all users", security = @SecurityRequirement(name = "bearerAuth"), responses = @ApiResponse(responseCode = "200", description = "List of users retrieved"))`
    - `@PreAuthorize("isAuthenticated()")`
- Описание: Возвращает список всех пользователей, хранящихся в базе данных.

```java
public List<User> getAllUsers() {
    return userRepository.findAll();
}
```

### Создание нового пользователя

Метод для создания нового пользователя.

- URL: `/api/users`
- Метод: `POST`
- Аннотации:
    - `@Operation(summary = "Create a new user", security = @SecurityRequirement(name = "bearerAuth"), responses = @ApiResponse(responseCode = "200", description = "User created"))`
    - `@PreAuthorize("isAuthenticated()")`
- Описание: Создает и сохраняет нового пользователя в базе данных.

```java
public User createUser(@RequestBody User user) {
    return userRepository.save(user);
}
```

### Обновление существующего пользователя

Метод для обновления существующего пользователя.

- URL: `/api/users/{id}`
- Метод: `PUT`
- Аннотации:
    - `@Operation(summary = "Update an existing user", security = @SecurityRequirement(name = "bearerAuth"), responses = @ApiResponse(responseCode = "200", description = "User updated"))`
    - `@PreAuthorize("isAuthenticated()")`
- Описание: Обновляет данные существующего пользователя по его ID.

```java
public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPicture(userDetails.getPicture());
        return userRepository.save(user);
    }
    throw new RuntimeException("User not found with id " + id);
}
```

### Удаление пользователя

Метод для удаления пользователя.

- URL: `/api/users/{id}`
- Метод: `DELETE`
- Аннотации:
    - `@Operation(summary = "Delete a user", security = @SecurityRequirement(name = "bearerAuth"), responses = @ApiResponse(responseCode = "200", description = "User deleted successfully"))`
    - `@PreAuthorize("isAuthenticated()")`
- Описание: Удаляет пользователя по его ID.

```java
public String deleteUser(@PathVariable Long id) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
        userRepository.delete(userOptional.get());
        return "User deleted successfully.";
    }
    throw new RuntimeException("User not found with id " + id);
}
```


## Возврат на главный README

[< Назад к README.md](../../../../README.md)
