package com.cinium.ticketapi.config;

import com.cinium.ticketapi.model.Usuario;
import com.cinium.ticketapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. Componente para encriptar contraseñas de forma segura
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 2. Conexión del guardia de seguridad con el repositorio de la Base de Datos
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado en la base de datos: " + username));
            
            return User.builder()
                    .username(usuario.getUsername())
                    .password(usuario.getPassword()) // Esta contraseña ya vendrá cifrada de la BD
                    .roles(usuario.getRol())
                    .build();
        };
    }
}