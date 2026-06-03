package com.cinium.ticketapi.controller;

import com.cinium.ticketapi.dto.TicketCompraDTO;
import com.cinium.ticketapi.model.Evento;
import com.cinium.ticketapi.model.Usuario;
import com.cinium.ticketapi.repository.EventoRepository;
import com.cinium.ticketapi.repository.UsuarioRepository;
import com.cinium.ticketapi.service.EmailService;
import com.cinium.ticketapi.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    @Autowired private TicketService ticketService;
    @Autowired private EmailService emailService;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private EventoRepository eventoRepository;

    @PostMapping("/comprar/{eventoId}")
    public ResponseEntity<?> comprarTicket(@PathVariable Long eventoId, @Valid @RequestBody TicketCompraDTO datosCompra) {
        
        String usernameAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioReal = usuarioRepository.findByUsername(usernameAutenticado).get();

        if (!usuarioReal.getEmail().equals(datosCompra.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Por seguridad, solo puedes comprar entradas usando el correo con el que te registraste."));
        }

        Evento evento = eventoRepository.findById(eventoId).orElseThrow();
        
        if (evento.getEntradasVendidas() >= evento.getCapacidadMaxima()) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Sold Out: El evento ha alcanzado su capacidad máxima."));
        }

        ticketService.comprarTicket(eventoId, datosCompra);

        
        emailService.enviarEmailTicket(datosCompra.getNombreComprador(), evento.getNombre(), datosCompra.getEmail());

        return ResponseEntity.ok(Map.of("mensaje", "Compra realizada con éxito. Revisa tu email."));
    }
}