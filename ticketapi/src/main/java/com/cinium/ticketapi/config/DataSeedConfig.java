package com.cinium.ticketapi.config;

import com.cinium.ticketapi.model.Evento;
import com.cinium.ticketapi.repository.EventoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeedConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSeedConfig.class);

    @Bean
    public CommandLineRunner iniciarDatos(EventoRepository eventoRepository) {
        return args -> {
            if (eventoRepository.count() == 0) {
                log.info("Base de datos vacía detectada. Creando eventos de prueba...");
                
                Evento evento1 = new Evento();
                evento1.setNombre("Concierto de Rock Inicial");
                evento1.setCapacidadMaxima(500);
                
                Evento evento2 = new Evento();
                evento2.setNombre("Obra de Teatro VIP");
                evento2.setCapacidadMaxima(50);
                
                eventoRepository.save(evento1);
                eventoRepository.save(evento2);
                
                log.info("Datos de prueba iniciales creados exitosamente.");
            } else {
                log.info("La base de datos ya contiene información. Omitiendo la carga inicial de semilla.");
            }
        };
    }
}