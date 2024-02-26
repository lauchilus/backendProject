package com.gamelist.main.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;



@OpenAPIDefinition(
		info = @Info(
				contact = @Contact(
				name = "lauchilus",
				email = "lautarofalfan@gmail.com"
					),
				description = "Documentation",
				title = "Gamelist backend",
				version = "1.0.0"
				)
		
		
		)
@SecurityScheme(
		name = "bearerAuth",
		description = "JWT Auth Description",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
		)
public class OpenApiConfig {

}
