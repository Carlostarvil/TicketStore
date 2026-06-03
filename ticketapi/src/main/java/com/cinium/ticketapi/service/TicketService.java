package com.cinium.ticketapi.service;

import com.cinium.ticketapi.dto.TicketCompraDTO;
import com.cinium.ticketapi.model.Evento;
import com.cinium.ticketapi.model.Ticket;
import com.cinium.ticketapi.repository.EventoRepository;
import com.cinium.ticketapi.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {

    // 1. Instanciamos el Logger
    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Transactional
    public Ticket comprarTicket(Long eventoId, TicketCompraDTO datosCompra) {
        log.info("Iniciando proceso de compra para el evento ID: {} por el usuario: {}", eventoId, datosCompra.getEmail());

        // 2. Validar si el evento existe
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> {
                    log.error("Fallo de compra: El evento ID {} no existe en la base de datos.", eventoId);
                    return new RuntimeException("Error: El evento no existe.");
                });

        // 3. Control de Aforo
        long ticketsVendidos = ticketRepository.findAll().stream()
                .filter(t -> t.getEvento().getId().equals(eventoId))
                .count();

        if (ticketsVendidos >= evento.getCapacidadMaxima()) {
            log.warn("Alerta de aforo: Intento de compra rechazado. El evento '{}' (ID: {}) ha alcanzado su capacidad máxima de {}.", 
                     evento.getNombre(), evento.getId(), evento.getCapacidadMaxima());
            throw new RuntimeException("Error: No quedan entradas disponibles para este evento. ¡Aforo completo!");
        }

        // 4. Crear el Ticket
        Ticket nuevoTicket = new Ticket();
        
        nuevoTicket.setNombreComprador(datosCompra.getNombreComprador());
        nuevoTicket.setEmailComprador(datosCompra.getEmail());
        nuevoTicket.setEvento(evento);

        Ticket ticketGuardado = ticketRepository.save(nuevoTicket);
        
        // 5. Registrar el éxito
        log.info("Compra exitosa: Ticket ID {} generado para el evento '{}' (Comprador: {})", 
                 ticketGuardado.getId(), evento.getNombre(), datosCompra.getEmail());

        return ticketGuardado;
    }
}