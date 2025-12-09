package com.guilherme.encurtador_url.config.exception;

import com.guilherme.encurtador_url.url.exception.UrlInputException;
import com.guilherme.encurtador_url.url.exception.UrlNãoExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlInputException.class)
    public ResponseEntity<ApiError> UrlInputHandle(UrlInputException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UrlNãoExistenteException.class)
    public ResponseEntity<ApiError> UrlNãoExistenteHandle(UrlNãoExistenteException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
