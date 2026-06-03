package com.cinium.ticketapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // 1. La llave secreta para encriptar
    private static final String SECRET_KEY = "MiClaveSecretaSuperSeguraParaElProyectoDeTickets2026_MasLargaYRobusta!";
    
    // Convertimos el texto en una clave criptográfica real
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 2. Extraer el nombre de usuario del Token
    public String extraerUsername(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // 3. Generar un Token nuevo para un usuario que acaba de hacer login
    public String generarToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // A quién pertenece
                .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Caduca en 10 horas
                .signWith(key, SignatureAlgorithm.HS256) // Firma matemática
                .compact();
    }

    // 4. Validar si el Token es correcto y no ha caducado
    public boolean validarToken(String token, UserDetails userDetails) {
        final String username = extraerUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpirado(token));
    }

    private boolean isTokenExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}