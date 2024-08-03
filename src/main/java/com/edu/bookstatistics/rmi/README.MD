# RMI конфигурация и удалённый сервис для bookstatistics

В этом разделе подробно описан код, используемый для реализации конфигурации RMI и удаленного сервиса для сущности `Book` в проекте `bookstatistics`. Здесь мы обсудим как настройки RMI, так и реализацию удаленного сервиса.

## Навигация

- [BookRemoteServiceImpl](#bookremoteserviceimpl)
  - [Описание](#описание)
  - [Методы](#методы)
  - [Конвертация DTO и сущностей](#конвертация-dto-и-сущностей)
  - [Пример использования](#пример-использования)
- [RMIConfig](#rmiconfig)
  - [Описание](#описание-1)
  - [Методы](#методы-1)
  - [Пример настройки](#пример-настройки)
- [Возврат на главный README](#возврат-на-главный-readme)

## BookRemoteServiceImpl

### Описание

`BookRemoteServiceImpl` реализует интерфейс `BookRemoteService` и предоставляет методы для доступа к данным о книгах через RMI (Remote Method Invocation). Это позволяет клиентам вызывать методы на удаленном сервере.

### Методы

- **getAllBooks(int page, int size)**
  - **Описание:** Возвращает список книг с поддержкой пагинации.
  - **Параметры:** 
    - `page` — номер страницы
    - `size` — размер страницы
  - **Возвращает:** Список `BookRMI`

- **getBook(Long id)**
  - **Описание:** Возвращает книгу по ID.
  - **Параметры:** 
    - `id` — ID книги
  - **Возвращает:** `BookRMI`

- **addBook(BookRMI bookDTO)**
  - **Описание:** Добавляет новую книгу.
  - **Параметры:** 
    - `bookDTO` — DTO книги
  - **Возвращает:** Добавленную книгу в виде `BookRMI`

- **updateBook(BookRMI bookDTO)**
  - **Описание:** Обновляет существующую книгу.
  - **Параметры:** 
    - `bookDTO` — DTO книги
  - **Возвращает:** Обновленную книгу в виде `BookRMI`

- **deleteBook(Long id)**
  - **Описание:** Удаляет книгу по ID.
  - **Параметры:** 
    - `id` — ID книги

### Конвертация DTO и сущностей

- **toDTO(Book book)**
  - Преобразует сущность `Book` в объект `BookRMI`.

- **toEntity(BookRMI dto)**
  - Преобразует объект `BookRMI` в сущность `Book`.

### Пример использования

```java
// Пример использования внутри Spring Bean
@Autowired
private BookRemoteService bookRemoteService;

// Получение всех книг
List<BookRMI> books = bookRemoteService.getAllBooks(0, 10);

// Добавление новой книги
BookRMI newBook = new BookRMI();
newBook.setTitle("Новое заглавие");
newBook.setAuthor("Автор");
BookRMI addedBook = bookRemoteService.addBook(newBook);

// Поиск книги по ID
BookRMI book = bookRemoteService.getBook(1L);
```

## RMIConfig

### Описание

`RMIConfig` конфигурирует RMI сервер и регистрирует удалённый сервис `BookRemoteServiceImpl`.

### Методы

- **registry()**
    - **Описание:** Настраивает и создает `Registry` для RMI, а также регистрирует `BookRemoteService`.
    - **Возвращает:** Сконфигурированное RMI `Registry`.

### Пример настройки

```java
// Автоматическое внедрение через Spring
@Autowired
private Registry registry;

// Получение удаленного сервиса
BookRemoteService bookRemoteService = (BookRemoteService) registry.lookup("BookRemoteService");
```

## Возврат на главный README

[Возврат на рутовый README.md](../../../../README.md)