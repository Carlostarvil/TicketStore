package com.cinium.ticketapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Activación del soporte CORS mapeado con nuestro origen configurado
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() 
                .requestMatchers(HttpMethod.GET, "/api/eventos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/tickets/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                // Permite de forma explícita que las consultas de control OPTIONS pasen sin autenticación
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Escudos de seguridad basados en roles para Eventos
                .requestMatchers(HttpMethod.POST, "/api/eventos", "/api/eventos/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/eventos/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                
                // --- REGLA ACTUALIZADA: Todo el panel de usuarios es exclusivo de ADMIN ---
                .requestMatchers("/api/usuarios/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                // --------------------------------------------------------------------------
                
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 2. Definición explícita de la política CORS global de la infraestructura
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Establece el origen autorizado de confianza
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        
        // Determina qué métodos HTTP están permitidos procesar desde el exterior
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // Declara qué encabezados HTTP puede enviar el cliente de forma segura
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        
        // Autoriza la transmisión de cookies o credenciales de sesión en las peticiones cruzadas
        configuration.setAllowCredentials(true);

        // Mapea esta configuración específica para que aplique a todas las rutas de la API
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}