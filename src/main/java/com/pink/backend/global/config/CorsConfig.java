package com.pink.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private static final String[] ALLOWED_ORIGINS = {
        "http://localhost:3000",
        "http://52.78.40.174",
        "http://code-bento.shop/",
        "https://code-bento.shop/"
    };

    private static final String[] ALLOWED_METHODS = {
        "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
    };

    private static final String[] ALLOWED_HEADERS = {
        "Accept", "Accept-Language", "Content-Language", "Content-Type",
        "Authorization", "X-API-Key", "X-Requested-With", "X-CSRF-Token"
    };

    private static final String[] EXPOSED_HEADERS = {
        "Content-Disposition", "X-Total-Count", "X-Page-Number", "Authorization"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(ALLOWED_ORIGINS)
            .allowedMethods(ALLOWED_METHODS)
            .allowedHeaders("*")
            .exposedHeaders(EXPOSED_HEADERS)
            .allowCredentials(true)
            .maxAge(3600);
    }
}

