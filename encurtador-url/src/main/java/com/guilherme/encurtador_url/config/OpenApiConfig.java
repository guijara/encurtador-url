package com.guilherme.encurtador_url.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Criamos um nome interno para a nossa configuração de segurança
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API Encurtador de URLs")
                        .description("API robusta para encurtamento de links com arquitetura escalável.")
                        .contact(new Contact().name("Guilherme Jara").email("guilhermerjara@hotmail.com"))
                        .version("1.0.0"))

                // Aplica o cadeado globalmente na API
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // Define os detalhes técnicos do cadeado (JWT Bearer)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}