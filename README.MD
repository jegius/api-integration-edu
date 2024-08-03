# Проект BookStatistics

`BookStatistics` — это учебный проект, который демонстрирует различные аспекты разработки на Java с использованием Spring Framework. Проект включает в себя управление книгами, пользователями, прогрессом чтения и применяет различные технологии для достижения этих целей, такие как RMI, gRPC, OAuth2, JWT и другие.

## Навигация

- [Описание проекта](#описание-проекта)
- [Используемые технологии](#используемые-технологии)
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

`BookStatistics` — проект, предназначенный для управления коллекцией книг, пользователей и прогресса чтения. Проект также интегрирован с сервисами аутентификации и авторизации с помощью OAuth2 и JWT, а также использует протоколы RMI и gRPC для удалённых вызовов методов.

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

## Сервисы для управления книгами, пользователями и прогрессом чтения

### BookService

- Описание: Методы для управления книгами (CRUD операции).
- документация находится [здесь](src/main/java/com/edu/bookstatistics/services/README.MD#bookservice)

### CustomOidcUserService

- Описание: Обработка OIDC запросов и сохранение информации о пользователях.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/services/README.MD#customoidcuserservice)

### GrpcBookServiceImpl

- Описание: Реализация gRPC сервиса для управления книгами.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/services/README.MD#grpcbookserviceimpl)

### ReadingProgressService

- Описание: Методы для управления прогрессом чтения книг.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/services/README.MD#readingprogressservice)

---

## RMI конфигурация и удалённый сервис

### BookRemoteServiceImpl

- Описание: Реализация удаленного RMI сервиса для управления книгами.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/rmi/README.MD#bookremoteserviceimpl)

### RMIConfig

- Описание: Конфигурация RMI сервера.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/rmi/README.MD#rmiconfig)

---

## Репозитории

### BookRepository

- Описание: Интерфейс для операций с базой данных для сущности `Book`.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/repositories/README.MD#bookrepository)

### ReadingProgressRepository

- Описание: Интерфейс для операций с базой данных для сущности `ReadingProgress`.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/repositories/README.MD#readingprogressrepository)

### UserRepository

- Описание: Интерфейс для операций с базой данных для сущности `User`.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/repositories/README.MD#userrepository)

---

## Сущности

### Book

- Описание: Сущность, представляющая книгу.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/entities/README.MD#book)

### ReadingProgress

- Описание: Сущность, представляющая прогресс чтения.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/entities/README.MD#readingprogress)

### User

- Описание: Сущность, представляющая пользователя.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/entities/README.MD#user)

---

## DTO

### BookDTO

- Описание: Объект передачи данных для книги.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/dto/README.MD#bookdto)

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
- документация находится [здесь](src/main/java/com/edu/bookstatistics/config/README.MD#securityconfig)

### CustomOAuth2SuccessHandler

- Описание: Пользовательский обработчик успешной аутентификации для OAuth2.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/config/README.MD#customoauth2successhandler)

### OpenApiConfig

- Описание: Конфигурация Swagger для документирования API.
- документация находится [здесь](src/main/java/com/edu/bookstatistics/config/README.MD#openapiconfig)

---