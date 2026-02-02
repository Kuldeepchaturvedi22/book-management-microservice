package com.bookservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Service API")
                        .description("REST API for managing books")
                        .version("1.0.0"));
    }
}