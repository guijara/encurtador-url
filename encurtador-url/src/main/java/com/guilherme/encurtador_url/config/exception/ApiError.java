package com.guilherme.encurtador_url.config.exception;

import java.time.LocalDateTime;

public record ApiError(String message, LocalDateTime timestamp) {
}
