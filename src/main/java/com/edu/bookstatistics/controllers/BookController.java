package com.edu.bookstatistics.controllers;

import com.edu.bookstatistics.dto.BookDTO;
import com.edu.bookstatistics.entities.Book;
import com.edu.bookstatistics.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Controller", description = "API для управления книгами")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(
            summary = "Добавить новую книгу",
            description = "Добавляет новую книгу в коллекцию",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные книги",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookDTO.class),
                            examples = @ExampleObject(value = "{\"title\": \"Новое название\", \"author\": \"Автор\", \"totalPages\": 44}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное добавление книги", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<Book> addBook(@RequestBody BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setTotalPages(bookDTO.getTotalPages());
        book.setAuthor(bookDTO.getAuthor());
        book.setCoverImage(bookDTO.getCoverImage());

        return ResponseEntity.ok(bookService.addBook(book));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    @Operation(
            summary = "Обновить книгу",
            description = "Обновляет информацию о книге",
            requestBody =  @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные обновленной книги",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BookDTO.class),
                            examples = @ExampleObject(value = "{\"id\": 1, \"title\": \"Обновленное название\", \"author\": \"Автор\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное обновление книги", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<Book> updateBook(@RequestBody BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setTotalPages(bookDTO.getTotalPages());
        book.setAuthor(bookDTO.getAuthor());
        book.setCoverImage(bookDTO.getCoverImage());

        return ResponseEntity.ok(bookService.updateBook(book));
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удалить книгу",
            description = "Удаляет книгу по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное удаление", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(
            summary = "Получить все книги",
            description = "Возвращает список всех книг с пагинацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.getAllBooks(page, size));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(
            summary = "Получить книгу по ID",
            description = "Возвращает информацию о книге по её ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешный запрос", content = @Content(mediaType = "application/json"))
            }
    )
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }
}