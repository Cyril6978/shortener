package com.shortener.shortener.controller;

import com.shortener.shortener.exception.InvalidUrlException;
import com.shortener.shortener.exception.ShortenerNotFoundException;
import com.shortener.shortener.service.ShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class ControllerAdvice {

    Logger logger = LoggerFactory.getLogger(ShortenerService.class);

    @ExceptionHandler(IOException.class)
    ResponseEntity<Void> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @ExceptionHandler(ShortenerNotFoundException.class)
    ResponseEntity<Void> handleShortenerNotFoundException(ShortenerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Void> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(InvalidUrlException.class)
    ResponseEntity<Void> handleInvalidUrlException(InvalidUrlException e) {
        return ResponseEntity.status((HttpStatus.BAD_REQUEST)).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).build();
    }
}
