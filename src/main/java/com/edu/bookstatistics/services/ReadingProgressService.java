package com.edu.bookstatistics.services;

import com.edu.bookstatistics.entities.ReadingProgress;
import com.edu.bookstatistics.repositories.ReadingProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingProgressService {

    private final ReadingProgressRepository progressRepository;

    public ReadingProgressService(ReadingProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public ReadingProgress addProgress(ReadingProgress progress) {
        return progressRepository.save(progress);
    }

    public List<ReadingProgress> getProgressByBookId(Long bookId) {
        return progressRepository.findByBookId(bookId);
    }
}