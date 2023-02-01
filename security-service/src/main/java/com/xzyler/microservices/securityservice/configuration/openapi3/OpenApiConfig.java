package com.xzyler.microservices.securityservice.configuration.openapi3;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Security Service API",
                version = "v2",
                description = "This app provides REST APIs for Security related features",
                contact = @Contact(
                        name = "Nitesh Thapa",
                        email = "xzyler.neetes@gmail.com",
                        url = "mailto://xzyler.neetes@gmail.com"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8088/",
                        description = "Development Server"
                )
        }
)
@SecurityScheme(
        name = "oauth2",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "Bearer",
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "http://localhost:8088/api/oauth/token"
                )
        )
)
public class OpenApiConfig {

}
