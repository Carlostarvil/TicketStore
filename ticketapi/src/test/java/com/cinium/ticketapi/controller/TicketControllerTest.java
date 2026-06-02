package com.cinium.ticketapi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testComprarTicketEmailInvalido_DeberiaDevolverErrorEstructurado() throws Exception {
        // Simulamos un JSON inválido (sin arroba en el email)
        String jsonInvalido = "{\"nombre\": \"Carlos\", \"email\": \"correo-malo\"}";

        mockMvc.perform(post("/api/tickets/comprar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                
                // 1. Verificamos el código HTTP
                .andExpect(status().isBadRequest())
                
                // 2. Verificamos que el JSON devuelto tenga la estructura de ErrorDetalles
                .andExpect(jsonPath("$.mensaje").value("El formato del email no es válido"))
                .andExpect(jsonPath("$.marcaDeTiempo").exists())
                .andExpect(jsonPath("$.detalles").exists());
    }
}