package com.shortener.shortener.controller;

import com.shortener.shortener.entity.LogErrMessage;
import com.shortener.shortener.exception.InvalidRemovalTokenException;
import com.shortener.shortener.exception.InvalidShortenerIdException;
import com.shortener.shortener.exception.InvalidUrlException;
import com.shortener.shortener.exception.ShortenerNotFoundException;
import com.shortener.shortener.service.ShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ControllerAdvice {
    @Autowired
    private ShortenerService shortenerService;
    Logger logger = LoggerFactory.getLogger(ShortenerService.class);

    @ExceptionHandler(IOException.class)
    ResponseEntity<Void> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @ExceptionHandler(ShortenerNotFoundException.class)
    ResponseEntity<Void> handleShortenerNotFoundException(ShortenerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(InvalidRemovalTokenException.class)
    ResponseEntity<Void> handleInvalidRemovalTokenException(InvalidRemovalTokenException e, HttpServletRequest request) {
        LogErrMessage errorMessage = new LogErrMessage();

        errorMessage.setMethod("deleteShortener");
        String ipAddress = request.getRemoteAddr();

        // Obtenez le chemin HTTP à partir de l'objet HttpServletRequest
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        String fullPath = requestURL.append('?').append(queryString).toString();
        errorMessage.setPathHttp(fullPath);

        errorMessage.setAdressIp(ipAddress);
        errorMessage.setTypeOfError("Error 403");
        errorMessage.setFileSrc("Shortener controller");
        errorMessage.setLine(158);
        errorMessage.setMessageError("le token est incorrect.");

        shortenerService.generateErrorMessage(errorMessage);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<Void> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(InvalidUrlException.class)
    ResponseEntity<?> handleInvalidUrlException(InvalidUrlException e, HttpServletRequest request) {
        int lineNumber = e.getStackTrace()[0].getLineNumber() - 1;

        LogErrMessage errorMessage = new LogErrMessage();
        String ipAddress = request.getRemoteAddr();
        errorMessage.setMethod(e.getStackTrace()[0].getMethodName());

        // Obtenez le chemin HTTP à partir de l'objet HttpServletRequest
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        String fullPath = requestURL.append('?').append(queryString).toString();
        errorMessage.setPathHttp(fullPath);

        errorMessage.setAdressIp(ipAddress);
        errorMessage.setTypeOfError("Error 400");
        errorMessage.setFileSrc(e.getStackTrace()[0].getFileName());
        errorMessage.setLine(lineNumber);
        errorMessage.setMessageError("invalid url");

        shortenerService.generateErrorMessage(errorMessage);


        return new ResponseEntity<>("invalid url", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status((HttpStatus.INTERNAL_SERVER_ERROR)).build();
    }

    @ExceptionHandler(InvalidShortenerIdException.class)
    ResponseEntity<Void> handleInvalidShortenerIdException(InvalidShortenerIdException e, HttpServletRequest request) {

        int lineNumber = e.getStackTrace()[0].getLineNumber() - 1;

        LogErrMessage errorMessage = new LogErrMessage();
        String ipAddress = request.getRemoteAddr();
        errorMessage.setMethod(e.getStackTrace()[0].getMethodName());

        // Obtenez le chemin HTTP à partir de l'objet HttpServletRequest
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        String fullPath = requestURL.append('?').append(queryString).toString();
        errorMessage.setPathHttp(fullPath);

        errorMessage.setAdressIp(ipAddress);
        errorMessage.setTypeOfError("not found");
        errorMessage.setFileSrc(e.getStackTrace()[0].getFileName());
        errorMessage.setLine(lineNumber);
        errorMessage.setMessageError(" l'Id n'est pas valide");

        shortenerService.generateErrorMessage(errorMessage);
        return ResponseEntity.status((HttpStatus.NOT_FOUND)).build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Void> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status((HttpStatus.NOT_FOUND)).build();
    }
}
