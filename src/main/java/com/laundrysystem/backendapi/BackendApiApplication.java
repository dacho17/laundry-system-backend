package com.laundrysystem.backendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApiApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/account/**").allowedOrigins(
					"http://localhost:3000",
					"https://client-coliv-demo.herokuapp.com");
				registry.addMapping("/availability/**").allowedOrigins(
					"http://localhost:3000",
					"https://client-coliv-demo.herokuapp.com");
				registry.addMapping("/booking/**").allowedOrigins(
					"http://localhost:3000",
					"https://client-coliv-demo.herokuapp.com");
			}
		};
	}
}
