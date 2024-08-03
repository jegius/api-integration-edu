package com.edu.bookstatistics.repositories;

import com.edu.bookstatistics.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
