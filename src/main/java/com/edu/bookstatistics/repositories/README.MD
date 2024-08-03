# Репозитории для bookstatistics

В этом разделе подробно описаны репозитории, используемые в проекте `bookstatistics`. Репозитории играют ключевую роль в доступе к данным, предоставляя методы для выполнения CRUD операций и дополнительных запросов.

## Навигация

- [BookRepository](#bookrepository)
  - [Описание](#описание)
  - [Методы](#методы)
  - [Аннотации и интерфейсы](#аннотации-и-интерфейсы)
- [ReadingProgressRepository](#readingprogressrepository)
  - [Описание](#описание-1)
  - [Методы](#методы-1)
  - [Аннотации и интерфейсы](#аннотации-и-интерфейсы-1)
- [UserRepository](#userrepository)
  - [Описание](#описание-2)
  - [Методы](#методы-2)
  - [Аннотации и интерфейсы](#аннотации-и-интерфейсы-2)
- [Возврат на главный README](#возврат-на-главный-readme)

## BookRepository

### Описание

`BookRepository` является интерфейсом, который предоставляет методы для работы с сущностью `Book`. Этот интерфейс наследуется от `JpaRepository` и предоставляет базовые CRUD операции без необходимости их реализации вручную.

### Методы

- Методы, унаследованные от `JpaRepository`:
  - `save`
  - `delete`
  - `findById`
- Методы, автоматически генерируемые Spring Data JPA:
  - Все методы, которые вы можете вызвать из `JpaRepository`

### Аннотации и интерфейсы

- Интерфейс `JpaRepository` является частью Spring Data JPA и предоставляет стандартные методы для работы с базой данных.

### Пример использования

```java
// Автоматическое внедрение через Spring
@Autowired
private BookRepository bookRepository;

// Сохранение книги
Book book = new Book();
book.setTitle("Новое Заглавие");
bookRepository.save(book);

// Поиск книги по ID
Optional<Book> book = bookRepository.findById(1L);
```

## ReadingProgressRepository

### Описание

`ReadingProgressRepository` является интерфейсом, который обеспечивает доступ к сущности `ReadingProgress`. Он также наследуется от `JpaRepository` и предоставляет дополнительные методы для получения данных по специфическим критериям.

### Методы

- `List<ReadingProgress> findByBookId(Long bookId)`: Возвращает список прогресса чтения для конкретной книги.
    - **Параметры**: `bookId` — ID книги.
    - **Возвращает**: Список объектов `ReadingProgress`.

### Аннотации и интерфейсы

- Интерфейс `JpaRepository`, который предоставляет стандартные методы для работы с базой данных.

### Пример использования

```java
// Автоматическое внедрение через Spring
@Autowired
private ReadingProgressRepository readingProgressRepository;

// Поиск прогресса чтения для книги с ID 1
List<ReadingProgress> progressList = readingProgressRepository.findByBookId(1L);
```

## UserRepository

### Описание

`UserRepository` является интерфейсом, который предоставляет методы для работы с сущностью `User`. Он наследуется от `JpaRepository` и предоставляет дополнительные методы для поиска пользователей по Google ID и email.

### Методы

- `Optional<User> findByGoogleId(String googleId)`: Возвращает опциональный объект `User`, найденного по Google ID.
    - **Параметры**: `googleId` — Google ID пользователя.
    - **Возвращает**: `Optional<User>`.

- `Optional<User> findByEmail(String email)`: Возвращает опциональный объект `User`, найденного по email.
    - **Параметры**: `email` — email пользователя.
    - **Возвращает**: `Optional<User>`.

### Аннотации и интерфейсы

- Интерфейс `JpaRepository`, который предоставляет стандартные методы для работы с базой данных.

### Пример использования

```java
// Автоматическое внедрение через Spring
@Autowired
private UserRepository userRepository;

// Поиск пользователя по Google ID
Optional<User> user = userRepository.findByGoogleId("123456789");

// Поиск пользователя по email
Optional<User> user = userRepository.findByEmail("example@example.com");
```

## Возврат на главный README

[Возврат на рутовый README.md](../../../../README.md)
