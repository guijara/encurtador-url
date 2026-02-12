package com.guilherme.encurtador_url.config.exception;

import com.guilherme.encurtador_url.url.exception.UrlConteudoException;
import com.guilherme.encurtador_url.url.exception.UrlFormatException;
import com.guilherme.encurtador_url.url.exception.UrlNãoExistenteException;
import com.guilherme.encurtador_url.user.exception.UserExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UrlConteudoException.class)
    public ResponseEntity<ApiError> UrlConteudoHandle(UrlConteudoException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiError> UserExisteException(UserExistsException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UrlNãoExistenteException.class)
    public ResponseEntity<ApiError> UrlNãoExistenteHandle(UrlNãoExistenteException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(UrlFormatException.class)
    public ResponseEntity<ApiError> UrlFormatHandle(UrlFormatException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> GenericExceptionHandle(Exception exception){

        logger.error("Erro inesperado no sistema ", exception);

        ApiError apiError = new ApiError("Ocorreu um erro interno do sistema. Por favor, tente mais tarde.",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> ArgumentoInvalidoException(MethodArgumentNotValidException exception){

        String errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiError apiError = new ApiError(
                "Erro de validação: " + errorMessages,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


}
