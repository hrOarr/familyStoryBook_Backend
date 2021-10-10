package com.astrodust.familyStoryBook_backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2UIConfig {
	
	@Bean
	public Docket Api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("Family-Story-Book-Api")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.astrodust.familyStoryBook_backend.controller"))
				.build();
	}
}
