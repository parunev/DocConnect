package com.parunev.docconnect.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(name = "DocConnect Team"),
                description = "A web-based platform that facilitates appointment booking with doctors in different medical fields.\n" +
                        "It also provides a doctor search feature to help users find specific doctors based on specialties and locations.",
                title = "DocConnect REST API"
        ),
        servers = {
                @Server(description = "Local Environment BE", url = "http://localhost:8080"),
                @Server(description = "Local Environment FE", url = "http://localhost:3000")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)

@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {
}
