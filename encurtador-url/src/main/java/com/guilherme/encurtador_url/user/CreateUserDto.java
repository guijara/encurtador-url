package com.guilherme.encurtador_url.user;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(@NotBlank(message = "Informe o usuário da conta.")
                            String username,
                            @NotBlank(message = "Informe a senha da conta.")
                            @Size(min = 6,message = "o tamanho mínimo da senha é de 6 caracteres")
                            String password) {
}
