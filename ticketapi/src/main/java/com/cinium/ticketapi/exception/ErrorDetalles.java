package com.cinium.ticketapi.exception;

import java.time.LocalDateTime;

public class ErrorDetalles {
    private LocalDateTime marcaDeTiempo;
    private String mensaje;
    private String detalles;

    public ErrorDetalles(LocalDateTime marcaDeTiempo, String mensaje, String detalles) {
        this.marcaDeTiempo = marcaDeTiempo;
        this.mensaje = mensaje;
        this.detalles = detalles;
    }

    // Getters
    public LocalDateTime getMarcaDeTiempo() { return marcaDeTiempo; }
    public String getMensaje() { return mensaje; }
    public String getDetalles() { return detalles; }
}