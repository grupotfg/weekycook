package com.grupotfg.weekycook.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI weekyCookOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WeekyCook API")
                        .description("Backend WeekyCook - Recetas, planes semanales y lista de la compra")
                        .version("v1.0")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio WeekyCook")
                        .url("https://github.com/grupotfg/weekycook"));
    }
}
