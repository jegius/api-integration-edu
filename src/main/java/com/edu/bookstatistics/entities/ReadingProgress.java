package com.edu.bookstatistics.entities;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity для прогресса чтения")
public class ReadingProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор", example = "1")
    private Long id;

    @Schema(description = "Дата прогресса чтения", example = "2023-10-12")
    private LocalDate date;

    @Schema(description = "Количество прочитанных страниц", example = "50")
    private int pagesRead;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @Schema(description = "Книга, к которой относится прогресс", implementation = Book.class)
    private Book book;
}