package com.fernando.connected_minds_api.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    public OpenAPI openAPI() {
        return new OpenAPI()
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
}