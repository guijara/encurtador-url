package com.guilherme.encurtador_url.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Encurtador de URLs")
                        .description("API robusta para encurtamento de links com arquitetura escalável.")
                        .contact(new Contact().name("Guilherme Jara").email("guilhermerjara@hotmail.com"))
                        .version("1.0.0"));
    }
}