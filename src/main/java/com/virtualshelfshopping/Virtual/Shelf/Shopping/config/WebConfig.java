package com.virtualshelfshopping.Virtual.Shelf.Shopping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Permitir todas as origens
                .allowedMethods("*") // Permitir todos os métodos
                .allowedHeaders("*") // Permitir todos os cabeçalhos
                .allowCredentials(false); // Permitir credenciais
    }
}
