package com.edu.bookstatistics.repositories;

import com.edu.bookstatistics.entities.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    List<ReadingProgress> findByBookId(Long bookId);
}
