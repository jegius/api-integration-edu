package com.edu.bookstatistics.controllers;

import com.edu.bookstatistics.entities.ReadingProgress;
import com.edu.bookstatistics.services.ReadingProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@Tag(name = "Reading Progress Controller", description = "API для управления прогрессом чтения")
public class ReadingProgressController {

    private final ReadingProgressService progressService;

    public ReadingProgressController(ReadingProgressService progressService) {
        this.progressService = progressService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(
            summary = "Добавить прогресс чтения",
            security = @SecurityRequirement(name = "bearerAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные прогресса чтения",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReadingProgress.class),
                            examples = @ExampleObject(value = "{\"date\": \"2023-10-12\", \"pagesRead\": 50, \"book\": {\"id\": 1}}")
                    )
            ),
            responses = @ApiResponse(responseCode = "200", description = "Успешное добавление прогресса")
    )
    public ResponseEntity<ReadingProgress> addProgress(@RequestBody ReadingProgress progress) {
        return ResponseEntity.ok(progressService.addProgress(progress));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/book/{bookId}")
    @Operation(
            summary = "Получить прогресс чтения по ID книги",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = @ApiResponse(responseCode = "200", description = "Успешный запрос")
    )
    public ResponseEntity<List<ReadingProgress>> getProgressByBookId(@PathVariable Long bookId) {
        return ResponseEntity.ok(progressService.getProgressByBookId(bookId));
    }
}