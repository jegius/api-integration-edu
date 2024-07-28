package com.edu.bookstatistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для книги")
public class BookDTO {
    @Schema(description = "Идентификатор книги", example = "1")
    private Long id;

    @Schema(description = "Название книги", example = "Пример названия")
    private String title;

    @Schema(description = "Общее количество страниц", example = "300")
    private int totalPages;

    @Schema(description = "Автор книги", example = "Пример автора")
    private String author;

    @Schema(description = "URL изображения обложки книги", example = "http://example.com/cover.jpg")
    private String coverImage;
}