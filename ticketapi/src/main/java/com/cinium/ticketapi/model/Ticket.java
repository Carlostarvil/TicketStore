package com.cinium.ticketapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- IMPORTANTE: Añade esta línea
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreComprador;
    private String emailComprador;

    // --- AQUÍ ESTÁ LA MAGIA PARA EVITAR EL BUCLE ---
    @JsonIgnore // <-- Esta anotación rompe el círculo vicioso
    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    // Constructor vacío obligatorio
    public Ticket() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getEmailComprador() {
        return emailComprador;
    }

    public void setEmailComprador(String emailComprador) {
        this.emailComprador = emailComprador;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}