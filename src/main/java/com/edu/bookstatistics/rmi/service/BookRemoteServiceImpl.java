package com.edu.bookstatistics.rmi.service;

import com.edu.bookstatistics.entities.Book;
import com.edu.bookstatistics.services.BookService;
import com.edu.dto.BookRMI;
import com.edu.rmi.BookRemoteService;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookRemoteServiceImpl extends UnicastRemoteObject implements BookRemoteService {

    private final BookService bookService;

    public BookRemoteServiceImpl(BookService bookService) throws RemoteException {
        super();
        this.bookService = bookService;
    }

    @Override
    public List<BookRMI> getAllBooks(int page, int size) throws RemoteException {
        return bookService.getAllBooks(page, size).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BookRMI getBook(Long id) throws RemoteException {
        Book book = bookService.getBook(id);
        return toDTO(book);
    }

    @Override
    public BookRMI addBook(BookRMI bookDTO) throws RemoteException {
        Book book = toEntity(bookDTO);
        book = bookService.addBook(book);
        return toDTO(book);
    }

    @Override
    public BookRMI updateBook(BookRMI bookDTO) throws RemoteException {
        Book book = toEntity(bookDTO);
        book = bookService.updateBook(book);
        return toDTO(book);
    }

    @Override
    public void deleteBook(Long id) throws RemoteException {
        bookService.deleteBook(id);
    }

    private BookRMI toDTO(Book book) {
        BookRMI dto = new BookRMI();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setTotalPages(book.getTotalPages());
        dto.setCoverImage(book.getCoverImage());
        return dto;
    }

    private Book toEntity(BookRMI dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setTotalPages(dto.getTotalPages());
        book.setCoverImage(dto.getCoverImage());
        return book;
    }
}