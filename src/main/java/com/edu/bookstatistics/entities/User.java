package com.edu.bookstatistics.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@Schema(description = "Сущность пользователя")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Электронная почта пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Google ID пользователя", example = "123456789")
    private String googleId;

    @Schema(description = "Имя пользователя", example = "John Doe")
    private String name;

    @Schema(description = "URL изображения пользователя", example = "http://example.com/picture.jpg")
    private String picture;
}