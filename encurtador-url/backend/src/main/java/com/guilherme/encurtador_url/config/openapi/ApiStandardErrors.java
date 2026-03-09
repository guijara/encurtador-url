package com.guilherme.encurtador_url.config.openapi;


import com.guilherme.encurtador_url.config.exception.ApiError;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "401",
                description = "Não autenticado (Token ausente ou inválido)",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Proibido (Sem permissão para este recurso)",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "429",
                description = "Muitas requisições (Rate Limit excedido)",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Erro interno do servidor",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        )
})
public @interface ApiStandardErrors {
}
