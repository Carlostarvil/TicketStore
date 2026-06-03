package com.cinium.ticketapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    // 1. Manejar errores de VALIDACIÓN (Ej: email sin arroba) -> Devuelve 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetalles> manejarErroresDeValidacion(MethodArgumentNotValidException ex, WebRequest request) {
        String mensajeError = ex.getBindingResult().getFieldError().getDefaultMessage();
        
        ErrorDetalles error = new ErrorDetalles(
                LocalDateTime.now(),
                mensajeError,
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // 2. Manejar REGLAS DE NEGOCIO (Ej: "El evento no existe") -> Devuelve 404 Not Found o 400
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetalles> manejarErroresGlobales(RuntimeException ex, WebRequest request) {
        ErrorDetalles error = new ErrorDetalles(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // Puedes dejar el 400 aquí para tus reglas de aforo
    }
    
    // 3. Manejar ERRORES CRÍTICOS DEL SERVIDOR (Ej: Se cayó la base de datos) -> Devuelve 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetalles> manejarExcepcionesGenerales(Exception ex, WebRequest request) {
        ErrorDetalles error = new ErrorDetalles(
                LocalDateTime.now(),
                "Error interno del servidor",
                request.getDescription(false)
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}