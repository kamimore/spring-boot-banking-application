package com.project.banking_app_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "The Java Academy Bank App", description = "Backend Rest Api's for tja Bank", version = "v1.0", contact = @Contact(name = "My name", email = "my email", url = "my url"), license = @License(
		name="the java academy", url="some url")), externalDocs = @ExternalDocumentation(
				description="The java academy description", url=" iama the url"))
public class BankingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppApplication.class, args);
	}

}
