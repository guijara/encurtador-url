package com.guilherme.encurtador_url.url.dto;

import com.guilherme.encurtador_url.url.ExpirationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UrlResponseCompleteDto(Long id,
                                     @Schema(description = "URL original completa", example = "https://google.com")String originalUrl,
                                     @Schema(description = "URL encurtada", example = "Q0u") String shortUrl,
                                     @Schema(description = "Número de ativações da URL") Integer numClicks,
                                     ExpirationType expirationType,
                                     @Schema(description = "Data de criação da URL encurtada")LocalDateTime expiredAt,
                                     @Schema(description = "Data de criação da URL encurtada")LocalDateTime creationAt) {
}
