package com.guilherme.encurtador_url.url.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UrlResponseDto(@Schema(description = "URL encurtada", example = "Q0u") String shortUrl) {
}
