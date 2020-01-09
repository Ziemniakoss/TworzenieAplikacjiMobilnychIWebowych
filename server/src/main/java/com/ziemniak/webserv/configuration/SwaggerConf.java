package com.ziemniak.webserv.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConf {

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).useDefaultResponseMessages(false).
				select()
				.apis(RequestHandlerSelectors.basePackage("com.ziemniak.webserv"))
				.build()
				.apiInfo(apiDetails());
	}

	private ApiInfo apiDetails(){
		return new ApiInfo(
				"BiblioZiem",
				"Aplikacja na zaliczenie zajęć projektowych z Programowania Aplikacji Mobilnych i Webowych",
				"1.0",
				"",
				new Contact("Przemysław Rawa","https://github.com/Ziemniakoss","przykładowy.mail@gmail.com"),
				"MIT",
				"https://opensource.org/licenses/mit-license.php",
				new ArrayList<>()
		);
	}
}
