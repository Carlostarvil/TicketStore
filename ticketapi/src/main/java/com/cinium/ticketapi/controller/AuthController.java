package com.cinium.ticketapi.controller;

import com.cinium.ticketapi.config.JwtUtil;
import com.cinium.ticketapi.dto.AuthRequestDTO;
import com.cinium.ticketapi.model.Usuario;
import com.cinium.ticketapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> crearTokenAutenticacion(@RequestBody AuthRequestDTO authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception e) {
            // NUEVO: Respuesta estructurada en vez de excepción
            return ResponseEntity.status(401).body(Map.of("mensaje", "Usuario o contraseña incorrectos"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generarToken(userDetails);
        Usuario usuario = usuarioRepository.findByUsername(authRequest.getUsername()).get();

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("jwt", jwt);
        respuesta.put("rol", usuario.getRol());
        respuesta.put("email", usuario.getEmail()); 

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        
        // --- NUEVO: Devolvemos JSON específicos en vez de explotar con un RuntimeException ---
        if (usuarioRepository.findByUsername(nuevoUsuario.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El nombre de usuario ya está en uso. Elige otro."));
        }

        if (nuevoUsuario.getEmail() != null && usuarioRepository.findByEmail(nuevoUsuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Este correo electrónico ya está registrado."));
        }
        // -------------------------------------------------------------------------------------

        String passwordCifrada = passwordEncoder.encode(nuevoUsuario.getPassword());
        nuevoUsuario.setPassword(passwordCifrada);
        
        if (nuevoUsuario.getRol() == null || nuevoUsuario.getRol().isEmpty()) {
            nuevoUsuario.setRol("USER");
        } else {
            nuevoUsuario.setRol(nuevoUsuario.getRol().toUpperCase());
        }

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Cuenta creada. Ya puedes iniciar sesión.");
        respuesta.put("username", usuarioGuardado.getUsername());
        respuesta.put("rol", usuarioGuardado.getRol());
        respuesta.put("email", usuarioGuardado.getEmail()); 

        return ResponseEntity.ok(respuesta);
    }
}