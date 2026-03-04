package com.guilherme.encurtador_url.config.exception;

import com.guilherme.encurtador_url.url.exception.*;
import com.guilherme.encurtador_url.user.exception.UserExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UrlContentException.class)
    public ResponseEntity<ApiError> handleUrlContent(UrlContentException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiError> handleUserExists(UserExistsException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UrlNonExistentException.class)
    public ResponseEntity<ApiError> handleUrlNonExistent(UrlNonExistentException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(UrlFormatException.class)
    public ResponseEntity<ApiError> handleUrlFormat(UrlFormatException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFound(UsernameNotFoundException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException exception){
        ApiError apiError = new ApiError("Usuário ou senha inválidos",LocalDateTime.now());
        return ResponseEntity.status(HttpStatusCode.valueOf(401)).body(apiError);
    }

    @ExceptionHandler(UserNotAllowedException.class)
    public ResponseEntity<ApiError> handleUserNotAllowed(UserNotAllowedException exception){
        ApiError apiError = new ApiError(exception.getMessage(),LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(UrlExpiredTimeException.class)
    public ResponseEntity<ApiError>  handleUrlExpired(UrlExpiredTimeException exception){
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.GONE).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception){

        String errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiError apiError = new ApiError(
                "Erro de validação: " + errorMessages,
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception exception){

        logger.error("Erro inesperado no sistema ", exception);

        ApiError apiError = new ApiError("Ocorreu um erro interno do sistema. Por favor, tente mais tarde.",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}
