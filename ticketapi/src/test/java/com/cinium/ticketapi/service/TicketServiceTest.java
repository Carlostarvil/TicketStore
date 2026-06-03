package com.cinium.ticketapi.service;

import com.cinium.ticketapi.dto.TicketCompraDTO;
import com.cinium.ticketapi.model.Evento;
import com.cinium.ticketapi.model.Ticket;
import com.cinium.ticketapi.repository.EventoRepository;
import com.cinium.ticketapi.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void testComprarTicket_AforoCompleto_LanzaExcepcion() {
        // 1. Configuración del escenario simulado (GIVEN / MOCK SETUP)
        Long eventoId = 1L;
        
        Evento eventoSimulado = new Evento();
        eventoSimulado.setId(eventoId);
        eventoSimulado.setNombre("Concierto de Rock Privado");
        eventoSimulado.setCapacidadMaxima(1); // Configuramos aforo límite de 1 persona

        TicketCompraDTO datosCompra = new TicketCompraDTO();
        datosCompra.setNombreComprador("Carlos"); 
        datosCompra.setEmail("carlos@email.com"); 

        // Simulamos que ya se vendió un ticket previo para este mismo evento
        Ticket ticketExistente = new Ticket();
        ticketExistente.setEvento(eventoSimulado);
        
        List<Ticket> listaTicketsExistentes = new ArrayList<>();
        listaTicketsExistentes.add(ticketExistente);

        // Programamos el comportamiento de nuestros Mocks de datos
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.of(eventoSimulado));
        when(ticketRepository.findAll()).thenReturn(listaTicketsExistentes);

        // 2. Ejecución y Verificación de la regla de negocio (WHEN / THEN / ASSERT)
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            ticketService.comprarTicket(eventoId, datosCompra);
        });

        // Validamos que el mensaje de error de aforo sea exactamente el programado
        assertEquals("Error: No quedan entradas disponibles para este evento. ¡Aforo completo!", excepcion.getMessage());
    }
}