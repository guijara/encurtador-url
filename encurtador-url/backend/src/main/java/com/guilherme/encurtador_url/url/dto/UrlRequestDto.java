package com.guilherme.encurtador_url.url.dto;

import com.guilherme.encurtador_url.url.ExpirationType;
import io.swagger.v3.oas.annotations.media.Schema;

public record UrlRequestDto(@Schema(description = "URL original completa",
        example = "https://google.com") String url,
        @Schema(description = "Quanto tempo deseja que a URL seja armazenada",
        example = "SEVEN_DAYS")ExpirationType expirationType) {
}
