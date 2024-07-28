package com.edu.bookstatistics.entities;

import jakarta.persistence.*;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;


@Entity
@Data
@Schema(description = "Entity для книги")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор книги", example = "1")
    private Long id;

    @Schema(description = "Название книги", example = "Пример названия")
    private String title;

    @Schema(description = "Автор книги", example = "Пример автора")
    private String author;

    @Schema(description = "Общее количество страниц", example = "300")
    private int totalPages;

    @Schema(description = "URL изображения обложки книги", example = "http://example.com/cover.jpg")
    private String coverImage;
}