package com.example.bookly.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title       = "Bookly API",
                version     = "1.0.0",
                description = "REST API for managing a bookstore — books, authors, categories, orders, and user authentication.\n\n"
                        + "## Authentication\n"
                        + "Most write operations require a JWT token. Use the `/api/v1/auth/login` endpoint to obtain a token, "
                        + "then click the **Authorize** button above and enter your token.\n\n"
                        + "## Roles\n"
                        + "- **USER** — Can browse the catalog and place orders\n"
                        + "- **ADMIN** — Full access to manage books, authors, categories, users, and orders",
                contact     = @Contact(
                        name  = "Abdelrhman Samy",
                        email = "abdelrhmansamy558@gmail.com"
                ),
                license     = @License(
                        name = "MIT License",
                        url  = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "/", description = "Current Server (auto-detected)"),
                @Server(url = "https://bookly-rest-production.up.railway.app", description = "Railway Production"),
                @Server(url = "http://localhost:8080", description = "Local Development")
        }
)
@SecurityScheme(
        name             = "bearerAuth",
        type             = SecuritySchemeType.HTTP,
        scheme           = "bearer",
        bearerFormat     = "JWT",
        description      = "Enter the JWT access token obtained from the /api/v1/auth/login endpoint"
)
public class SwaggerConfig {}
