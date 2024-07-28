package com.edu.bookstatistics.services;

import com.edu.bookstatistics.entities.Book;
import com.edu.bookstatistics.repositiories.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(int page, int size) {
        return bookRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}
