package com.example.ex6.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



    // ✅ Traite la requête et la réponse pour ajouter les headers CORS
    @Configuration
    public class CorsConfig implements WebMvcConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
            .allowedOrigins("http://127.0.0.1:5501", "http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:5501") // ✅ No extra quotes
            .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
            .allowedHeaders("Content-Type", "Authorization")
            .allowCredentials(true)
            .maxAge(3600);
        }
    }

