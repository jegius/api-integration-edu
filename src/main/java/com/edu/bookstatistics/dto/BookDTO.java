package com.edu.bookstatistics.dto;

import lombok.Data;

@Data
public class BookDTO {
    private String title;
    private int totalPages;
    private String author;
    private String coverImage;
}