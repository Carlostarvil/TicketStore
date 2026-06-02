package com.cinium.ticketapi.dto;

public class AuthRequestDTO {

    private String username;
    private String password;

    // Constructores vacíos obligatorios para que Spring pueda leer el JSON
    public AuthRequestDTO() {}

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}