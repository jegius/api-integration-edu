package com.edu.bookstatistics.rmi;

import com.edu.bookstatistics.rmi.service.BookRemoteServiceImpl;
import com.edu.bookstatistics.services.BookService;
import com.edu.rmi.BookRemoteService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

@Configuration
public class RMIConfig {

    private final BookService bookService;

    public RMIConfig(BookService bookService) {
        this.bookService = bookService;
    }

    @Bean
    public Registry registry() throws Exception {
        Registry registry = LocateRegistry.createRegistry(1099);
        BookRemoteService bookRemoteService = new BookRemoteServiceImpl(bookService);
        registry.rebind("BookRemoteService", bookRemoteService);
        return registry;
    }
}

