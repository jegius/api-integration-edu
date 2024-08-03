# Сервисы для управления книгами, пользователями и прогрессом чтения

В этом разделе подробно описан код, используемый для управления различными аспектами проекта `bookstatistics`, включая книги, пользователей и прогресс чтения. Также рассмотрены реализации через сервисы RMI и gRPC.

## Навигация

- [Сервисы книг](#сервисы-книг)
  - [BookService](#bookservice)
    - [Описание](#описание)
    - [Методы](#методы)
    - [Пример использования](#пример-использования)
- [Сервис пользователей](#сервис-пользователей)
  - [CustomOidcUserService](#customoidcuserservice)
    - [Описание](#описание-1)
    - [Методы](#методы-1)
    - [Пример использования](#пример-использования-1)
- [gRPC сервис для книг](#grpc-сервис-для-книг)
  - [GrpcBookServiceImpl](#grpcbookserviceimpl)
    - [Описание](#описание-2)
    - [Методы](#методы-2)
    - [Пример использования](#пример-использования-2)
- [Прогресс чтения](#прогресс-чтения)
  - [ReadingProgressService](#readingprogressservice)
    - [Описание](#описание-3)
    - [Методы](#методы-3)
    - [Пример использования](#пример-использования-3)
- [Возврат на главный README](#возврат-на-главный-readme)

## Сервисы книг

### BookService

#### Описание

`BookService` предоставляет методы для управления книгами в системе. Этот сервис обрабатывает CRUD операции, включая добавление, удаление, обновление и получение книг.

#### Методы

- **addBook(Book book)**
  - **Описание:** Добавляет новую книгу в систему.
  - **Параметры:** 
    - `book` — сущность книги
  - **Возвращает:** Сохраненную книгу

- **deleteBook(Long id)**
  - **Описание:** Удаляет книгу по ID.
  - **Параметры:** 
    - `id` — ID книги

- **updateBook(Book book)**
  - **Описание:** Обновляет данные существующей книги.
  - **Параметры:** 
    - `book` — сущность книги
  - **Возвращает:** Обновленную книгу

- **getAllBooks(int page, int size)**
  - **Описание:** Возвращает список книг с поддержкой пагинации.
  - **Параметры:** 
    - `page` — номер страницы
    - `size` — размер страницы
  - **Возвращает:** Список книг

- **getBook(Long id)**
  - **Описание:** Возвращает книгу по ID.
  - **Параметры:** 
    - `id` — ID книги
  - **Возвращает:** Найденную книгу или `null`

#### Пример использования

```java
@Autowired
private BookService bookService;

// Добавление новой книги
Book newBook = new Book();
newBook.setTitle("Новое заглавие");
newBook.setAuthor("Автор");
bookService.addBook(newBook);

// Получение книги по ID
Book book = bookService.getBook(1L);
```

## Сервис пользователей

### CustomOidcUserService

#### Описание

`CustomOidcUserService` обрабатывает OAuth 2.0 запросы пользователей через Google и сохраняет информацию о пользователях в базу данных.

#### Методы

- **loadUser(OidcUserRequest oidcUserRequest)**
    - **Описание:** Загружает пользователя по OIDC запросу и сохраняет его данные в базу.
    - **Параметры:**
        - `oidcUserRequest` — запрос OIDC пользователя
    - **Возвращает:** `OidcUser`

#### Пример использования

```java
@Autowired
private CustomOidcUserService oidcUserService;

// Загрузка OIDC пользователя
OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);
```

## gRPC сервис для книг

### GrpcBookServiceImpl

#### Описание

`GrpcBookServiceImpl` реализует gRPC сервис для управления книгами, предоставляя методы для добавления книг и возвращения их информации.

#### Методы

- **addBook(BookRequest request, StreamObserver<BookResponse> responseObserver)**
    - **Описание:** Добавляет книгу через gRPC запрос.
    - **Параметры:**
        - `request` — запрос на добавление книги
        - `responseObserver` — наблюдатель для ответа
    - **Возвращает:** Ответ с данными книги

#### Пример использования

```java
// gRPC клиентский код (пример)
BookRequest request = BookRequest.newBuilder()
    .setTitle("Новое заглавие")
    .setAuthor("Автор")
    .setTotalPages(300)
    .setCoverImage("image_url")
    .build();

bookServiceStub.addBook(request, new StreamObserver<BookResponse>(){
    @Override
    public void onNext(BookResponse value) {
        System.out.println("Книга добавлена: " + value.getId());
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("Операция завершена");
    }
});
```

## Прогресс чтения

### ReadingProgressService

#### Описание

`ReadingProgressService` предоставляет методы для управления прогрессом чтения книг, включая добавление прогресса и получение прогресса по ID книги.

#### Методы

- **addProgress(ReadingProgress progress)**
    - **Описание:** Добавляет новый прогресс чтения.
    - **Параметры:**
        - `progress` — сущность прогресса чтения
    - **Возвращает:** Сохраненный прогресс

- **getProgressByBookId(Long bookId)**
    - **Описание:** Возвращает список прогресса чтения по ID книги.
    - **Параметры:**
        - `bookId` — ID книги
    - **Возвращает:** Список прогресса чтения

#### Пример использования

```java
@Autowired
private ReadingProgressService progressService;

// Добавление прогресса чтения
ReadingProgress progress = new ReadingProgress();
progress.setBookId(1L);
progress.setPageNumber(50);
progressService.addProgress(progress);

// Получение прогресса по ID книги
List<ReadingProgress> progressList = progressService.getProgressByBookId(1L);
```

## Возврат на главный README

[Возврат на рутовый README.md](../../../../../README.md)