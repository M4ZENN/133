package com.example.ex6.config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SimpleCORSFilter implements Filter {

    // ✅ Traite la requête et la réponse pour ajouter les headers CORS
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;  
        HttpServletRequest request = (HttpServletRequest) req; 

        // ✅ Autorise toutes les origines, remplace "*" par une origine spécifique si nécessaire
        response.setHeader("Access-Control-Allow-Origin", "*");
        // ✅ Permet certaines méthodes HTTP pour CORS
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        // ✅ Définit les headers autorisés dans la requête
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // ✅ Durée de validité du CORS préflight (en secondes)
        response.setHeader("Access-Control-Max-Age", "3600");

        // ✅ Gère la requête OPTIONS (préflight) et répond immédiatement
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        chain.doFilter(req, res);  // ✅ Passe la requête au prochain filtre ou au contrôleur
    }
}
