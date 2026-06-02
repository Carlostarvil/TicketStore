package com.cinium.ticketapi.controller;

import com.cinium.ticketapi.model.Evento;
import com.cinium.ticketapi.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/eventos")
@CrossOrigin(origins = "http://localhost:5173")
public class EventoController {

    // --- NUEVO: Motor de registro para la consola ---
    private static final Logger log = LoggerFactory.getLogger(EventoController.class);

    @Autowired
    private EventoRepository eventoRepository;

    // 1. Guardar evento y LIMPIAR la caché obsoleta
    @PostMapping
    @CacheEvict(value = "eventos", allEntries = true)
    public Evento crearEvento(@RequestBody Evento nuevoEvento) {
        // --- NUEVO: Auditoría de datos entrantes ---
        log.info("--- NUEVA PETICIÓN DE CREACIÓN DE EVENTO ---");
        log.info("Nombre: {}", nuevoEvento.getNombre());
        log.info("Fecha: {}", nuevoEvento.getFecha());
        log.info("Hora Inicio: {}", nuevoEvento.getHoraInicio());
        log.info("Hora Fin: {}", nuevoEvento.getHoraFin());
        
        return eventoRepository.save(nuevoEvento);
    }

    // 2. Leer eventos desde la MEMORIA RAM si ya han sido consultados
    @GetMapping
    @Cacheable(value = "eventos")
    public Page<Evento> obtenerTodosLosEventos(@PageableDefault(size = 10) Pageable pageable) {
        return eventoRepository.findAll(pageable);
    }

    // 3. Eliminar evento y LIMPIAR la caché obsoleta
    @DeleteMapping("/{id}")
    @CacheEvict(value = "eventos", allEntries = true)
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        if (!eventoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("mensaje", "Evento eliminado de la base de datos"));
    }
}