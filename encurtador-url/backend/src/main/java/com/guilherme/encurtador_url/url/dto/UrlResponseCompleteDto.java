package com.guilherme.encurtador_url.url.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UrlResponseCompleteDto(@Schema(description = "URL original completa", example = "https://google.com")String originalUrl,
                                     @Schema(description = "URL encurtada", example = "Q0u") String shortUrl,
                                     @Schema(description = "Número de ativações da URL") Integer numClicks,
                                     @Schema(description = "Data de criação da URL encurtada")LocalDateTime creationAt) {
}
