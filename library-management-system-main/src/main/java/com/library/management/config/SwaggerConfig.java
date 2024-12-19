package com.library.management.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("library-management")
                .pathsToMatch("/api/books/**", "/api/borrowers/**", "/api/**")
                .build();
    }

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .version("1.0")
                        .description("API documentation for the Library Management System")
                        .contact(new Contact().name("Your Name").email("your.email@example.com"))
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", 
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                );
    }
}


/*
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("library-management")
                .pathsToMatch("/api/books/**", "/api/borrowers/**")
                .build();
    }

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new Info()
                        .title("Library Management System API")
                        .version("1.0")
                        .description("API documentation for the Library Management System")
                        .contact(new Contact().name("Your Name").email("your.email@example.com"))
                );
    }
}*/
