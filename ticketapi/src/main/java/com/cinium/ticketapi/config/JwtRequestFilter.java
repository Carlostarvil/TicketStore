package com.cinium.ticketapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 1. Extraer la cabecera "Authorization" de la petición
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Comprobar si el token existe y empieza por la palabra "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Extraemos el token quitando "Bearer " (7 caracteres)
            username = jwtUtil.extraerUsername(jwt); // Usamos tu JwtUtil para sacar el nombre
        }

        // 3. Si encontramos un usuario y no está ya autenticado en este contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos los datos reales del usuario desde nuestro sistema
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validamos que el token no sea inventado ni caducado
            if (jwtUtil.validarToken(jwt, userDetails)) {

                // 4. Si es válido, creamos una credencial de acceso oficial para Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 5. Guardamos esta credencial en la memoria de Spring Security para el resto de la petición
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // 6. Pase lo que pase, dejamos que la petición continúe su camino hacia el siguiente filtro o controlador
        chain.doFilter(request, response);
    }
}