package com.guilherme.encurtador_url.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(@NotBlank(message = "Informe o usuário da conta.")
                             @Schema(description = "nome de usuário da conta", example = "guilherminho123") String username,
                            @NotBlank(message = "Informe a senha da conta.")
                            @Size(min = 6,message = "o tamanho mínimo da senha é de 6 caracteres")
                            @Schema(description = "senha para conectar o usuário", example = "liko0092%") String password) {
}
