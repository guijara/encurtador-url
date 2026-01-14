package com.guilherme.encurtador_url.url.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UrlRequestDto(@Schema(description = "URL original completa", example = "https://google.com") String url) {
}
