package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors. UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * CORS Configuration for allowing frontend to communicate with backend
 *
 * WHY NEEDED?
 * - Frontend runs on http://localhost:3000
 * - Backend runs on http://localhost:8080
 * - Browser blocks cross-origin requests by default (security)
 * - This configuration allows specific origins to access our API
 */
@Configuration

public class CorsConfig {

    /**
     * Configure CORS globally
     *
     * This allows:
     * - Frontend at localhost:3000 to access backend
     * - All HTTP methods (GET, POST, PUT, DELETE, etc.)
     * - All headers (Authorization, Content-Type, etc.)
     * - Credentials (cookies, authorization headers)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ===== ALLOWED ORIGINS ===== //
        // Add your frontend URL here
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",      // Next.js dev server
                "http://127.0.0.1:3000",      // Alternative localhost
                "http://localhost:3001",      // Backup port
                "https://yourdomain.com"      // Production domain (add when deploying)
        ));

        // ===== ALLOWED METHODS ===== //
        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays. asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
        ));

        // ===== ALLOWED HEADERS ===== //
        // Allow all headers (especially Authorization for JWT)
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
        ));

        // ===== EXPOSED HEADERS ===== //
        // Headers that frontend can access in response
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        // ===== ALLOW CREDENTIALS ===== //
        // Allow sending cookies and authorization headers
        configuration.setAllowCredentials(true);

        // ===== MAX AGE ===== //
        // How long browser can cache preflight request results (in seconds)
        configuration.setMaxAge(3600L); // 1 hour

        // Apply configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}