package com.fernando.connected_minds_api.swagger;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
    public OpenAPI openAPI() {
        return new OpenAPI()
            .addSecurityItem(
                new SecurityRequirement()
                .addList("bearer")
                )
                .components(
                    new Components()
                    .addSecuritySchemes("bearer", createJWTAPIScheme())
                )
            .info(
                new Info()
                    .title("Connected Minds API")
                    .description("This is the API from Connected Minds social network")
                    .contact(
                        new Contact()
                            .name("Fernando de Barros")
                            .email("fdebarros0910-2004@hotmail.com")
                            .url("https://www.linkedin.com/in/fernando-de-barros-204864241/")
                    )
            );
    }

    private SecurityScheme createJWTAPIScheme() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .scheme("bearer");
    }
}