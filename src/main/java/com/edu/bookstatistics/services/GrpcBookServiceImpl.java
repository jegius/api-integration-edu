package com.edu.bookstatistics.services;

import com.deps.bookstatistics.BookServiceGrpc;
import com.deps.bookstatistics.BookRequest;
import com.deps.bookstatistics.BookResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GrpcBookServiceImpl extends BookServiceGrpc.BookServiceImplBase {
    @Override
    public void addBook(BookRequest request, StreamObserver<BookResponse> responseObserver) {
        BookResponse response = BookResponse.newBuilder()
                .setId("1")
                .setTitle(request.getTitle())
                .setAuthor(request.getAuthor())
                .setTotalPages(request.getTotalPages())
                .setCoverImage(request.getCoverImage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}