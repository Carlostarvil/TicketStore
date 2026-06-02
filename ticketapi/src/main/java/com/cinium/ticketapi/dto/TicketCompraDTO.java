package com.cinium.ticketapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TicketCompraDTO {
    
    // Cambiamos "nombre" por "nombreComprador" para que coincida con el controlador
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombreComprador;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email no es válido")
    private String email;

    // Getters y Setters actualizados
    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}