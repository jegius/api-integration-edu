# Проект BookStatistics

`BookStatistics` — это учебный проект, который демонстрирует различные аспекты разработки на Java с использованием Spring Framework. Проект включает в себя управление книгами, пользователями, прогрессом чтения и применяет различные технологии для достижения этих целей, такие как RMI, gRPC, OAuth2, JWT и другие.

## Навигация

- [Описание проекта](#описание-проекта)
- [Используемые технологии](#используемые-технологии)
- [Настройка проекта](#настройка-проекта)
    - [Настройка Docker и PostgreSQL](#настройка-docker-и-postgresql)
    - [Конфигурация приложения](#конфигурация-приложения)
    - [Настройка подключения к базе данных](#настройка-подключения-к-базе-данных)
    - [Настройка Google OAuth2](#настройка-google-oauth2)
- [Сервисы для управления книгами, пользователями и прогрессом чтения](#сервисы-для-управления-книгами-пользователями-и-прогрессом-чтения)
    - [BookService](#bookservice)
    - [CustomOidcUserService](#customoidcuserservice)
    - [GrpcBookServiceImpl](#grpcbookserviceimpl)
    - [ReadingProgressService](#readingprogressservice)
- [RMI конфигурация и удалённый сервис](#rmi-конфигурация-и-удалённый-сервис)
    - [BookRemoteServiceImpl](#bookremoteserviceimpl)
    - [RMIConfig](#rmiconfig)
- [Репозитории](#репозитории)
    - [BookRepository](#bookrepository)
    - [ReadingProgressRepository](#readingprogressrepository)
    - [UserRepository](#userrepository)
- [Сущности](#сущности)
    - [Book](#book)
    - [ReadingProgress](#readingprogress)
    - [User](#user)
- [DTO](#dto)
    - [BookDTO](#bookdto)
- [Контроллеры](#контроллеры)
    - [BookController](#bookcontroller)
    - [OAuthController](#oauthcontroller)
    - [ReadingProgressController](#readingprogresscontroller)
    - [UserController](#usercontroller)
- [Конфигурации](#конфигурации)
    - [SecurityConfig](#securityconfig)
    - [CustomOAuth2SuccessHandler](#customoauth2successhandler)
    - [OpenApiConfig](#openapiconfig)

---

## Описание проекта

`BookStatistics` — проект, предназначенный для управления коллекцией книг, пользователей и прогрессом чтения. Проект также интегрирован с сервисами аутентификации и авторизации с помощью OAuth2 и JWT, а также использует протоколы RMI и gRPC для удалённых вызовов методов.

## Используемые технологии

Этот проект использует различные технологии и библиотеки Java:

- **Spring Boot** - для быстрой настройки и запуска приложения.
- **Spring Security** - для обеспечения безопасности приложения.
- **OAuth2** - для обработки аутентификации пользователей.
- **JWT (JSON Web Token)** - для авторизации пользователей.
- **Spring Data JPA** - для работы с базами данных.
- **RMI (Remote Method Invocation)** - для удалённых вызовов методов.
- **gRPC** - для реализации высокопроизводительных удалённых вызовов.
- **Swagger/OpenAPI** - для документирования API.
- **Lombok** - для снижения шаблонного кода.

---

## Настройка проекта

### Настройка Docker и PostgreSQL

Для запуска проекта необходимо настроить Docker и поднять контейнер с PostgreSQL. Для этого используйте следующий файл `docker-compose.yml`.

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:latest
    container_name: postgres_container
    environment:
      POSTGRES_DB: books_db            # Имя базы данных
      POSTGRES_USER: book_user      # Ваш логин
      POSTGRES_PASSWORD: password   # Ваш пароль
    ports:
      - "5432:5432"                      # Пробрасываем порт
    volumes:
      - postgres_data:/var/lib/postgresql/data # Для хранения данных между перезагрузками контейнера

volumes:
  postgres_data:
```

Запустите контейнер с помощью команды:

```bash
docker-compose up -d
```

### Конфигурация приложения

Создайте или обновите файл `application.properties` в вашем проекте следующим образом:

```properties
spring.application.name=book-statistics
springdoc.swagger-ui.path=/swagger-ui.html

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
grpc.server.port=9090
spring.datasource.url=jdbc:postgresql://localhost:5432/books_db
spring.datasource.username=book_user
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://www.googleapis.com/oauth2/v3/certs
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.security.oauth2.client.registration.google.client-id=ВАШ_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=ВАШ_CLIENT_SECRET
spring.security.oauth2.client.registration.google.scope=openid,profile,email
spring.security.oauth2.client.provider.google.authorization-uri=https://accounts.google.com/o/oauth2/auth
spring.security.oauth2.client.provider.google.token-uri=https://oauth2.googleapis.com/token
spring.security.oauth2.client.provider.google.user-info-uri=https://openidconnect.googleapis.com/v1/userinfo
spring.security.oauth2.client.provider.google.user-name-attribute=sub
```

### Настройка подключения к базе данных

После того как вы подняли контейнер с PostgreSQL и настроили конфигурацию приложения, база данных будет доступна по следующим параметрам:

- URL: `jdbc:postgresql://localhost:5432/books_db`
- Имя пользователя: `book_user`
- Пароль: `password`

Эти параметры уже добавлены в файл `application.properties`, что позволяет вашему приложению автоматически подключаться к базе данных при запуске.

### Настройка Google OAuth2

Для настройки Google OAuth2 аутентификации в вашем проекте выполните следующие шаги:

1. **Создайте проект в Google Developers Console:**
    - Перейдите на сайт [Google Developers Console](https://console.developers.google.com/).
    - Создайте новый проект или выберите существующий.

2. **Настройте OAuth2.0:**
    - Перейдите в раздел **APIs & Services** -> **Credentials**.
    - Нажмите на **Create Credentials** и выберите **OAuth 2.0 Client ID**.
    - Вам нужно будет настроить экран согласия OAuth (Consent Screen), если вы еще этого не сделали.
    - В настройках OAuth согласия задайте следующие данные:
        - **Application type**: Web application
        - **Name**: (Введите имя для вашего OAuth клиента)
        - **Authorized redirect URIs**: Добавьте URL, который будет использоваться для аутентификации, например `http://localhost:8080/login/oauth2/code/google`

3. **Получите Client ID и Client Secret:**
    - Запишите Client ID и Client Secret, они понадобятся для настройки вашего Spring Boot приложения.

4. **Обновите ваш `application.properties` файл:**
    - Замените `spring.security.oauth2.client.registration.google.client-id` и `spring.security.oauth2.client.registration.google.client-secret` на значения, полученные на предыдущем шаге.

```properties
spring.security.oauth2.client.registration.google.client-id=ВАШ_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=ВАШ_CLIENT_SECRET
```

Для запуска используйте команду:

```bash
mvn spring-boot:run
```

---

## Сервисы для управления книгами, пользователями и прогрессом чтения

### BookService

- Описание: Методы для управления книгами (CRUD операции).
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#bookservice)

### CustomOidcUserService

- Описание: Обработка OIDC запросов и сохранение информации о пользователях.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#customoidcuserservice)

### GrpcBookServiceImpl

- Описание: Реализация gRPC сервиса для управления книгами.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#grpcbookserviceimpl)

### ReadingProgressService

- Описание: Методы для управления прогрессом чтения книг.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#readingprogressservice)

---

## RMI конфигурация и удалённый сервис

### BookRemoteServiceImpl

- Описание: Реализация удаленного RMI сервиса для управления книгами.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#bookremoteserviceimpl)

### RMIConfig

- Описание: Конфигурация RMI сервера.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#rmiconfig)

---

## Репозитории

### BookRepository

- Описание: Интерфейс для операций с базой данных для сущности `Book`.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#bookrepository)

### ReadingProgressRepository

- Описание: Интерфейс для операций с базой данных для сущности `ReadingProgress`.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#readingprogressrepository)

### UserRepository

- Описание: Интерфейс для операций с базой данных для сущности `User`.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#userrepository)

---

## Сущности

### Book

- Описание: Сущность, представляющая книгу.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#book)

### ReadingProgress

- Описание: Сущность, представляющая прогресс чтения.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#readingprogress)

### User

- Описание: Сущность, представляющая пользователя.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#user)

---

## DTO

### BookDTO

- Описание: Объект передачи данных для книги.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#bookdto)

---

## Контроллеры

### BookController

- Описание: Контроллер для управления книгами.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#bookcontroller)

### OAuthController

- Описание: Контроллер для обработки OAuth2 аутентификации.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#oauthcontroller)

### ReadingProgressController

- Описание: Контроллер для управления прогрессом чтения.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#readingprogresscontroller)

### UserController

- Описание: Контроллер для управления пользователями.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#usercontroller)

---

## Конфигурации

### SecurityConfig

- Описание: Конфигурация безопасности для проекта.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#securityconfig)

### CustomOAuth2SuccessHandler

- Описание: Пользовательский обработчик успешной аутентификации для OAuth2.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#customoauth2successhandler)

### OpenApiConfig

- Описание: Конфигурация Swagger для документирования API.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/controllers/README.MD#openapiconfig)

---

[< Назад к README.md](README.md)
