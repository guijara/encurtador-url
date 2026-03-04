package com.guilherme.encurtador_url.url.exception;

public class UrlNonExistentException extends RuntimeException {
    public UrlNonExistentException(String message) {
        super(message);
    }
}
