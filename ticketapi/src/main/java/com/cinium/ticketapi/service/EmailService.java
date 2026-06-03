package com.cinium.ticketapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    
    @Async
    public void enviarEmailTicket(String nombreComprador, String nombreEvento, String emailDestino) {
        log.info("Iniciando envío de email real a {} para el evento '{}'...", emailDestino, nombreEvento);
        
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom("Ticketmaster API <tu_correo@gmail.com>"); 
            mensaje.setTo(emailDestino);
            mensaje.setSubject("🎟️ ¡Tus entradas para " + nombreEvento + " están confirmadas!");
            mensaje.setText("Hola " + nombreComprador + ",\n\n"
                    + "¡Gracias por tu compra en nuestro sistema!\n"
                    + "Tus entradas para el evento '" + nombreEvento + "' han sido registradas y aseguradas con éxito en la base de datos.\n\n"
                    + "¡Que disfrutes del evento!");

            mailSender.send(mensaje);
            log.info("Email enviado exitosamente a {}", emailDestino);
            
        } catch (Exception e) {
            log.error("Fallo crítico al enviar el email a {}: {}", emailDestino, e.getMessage());
        }
    }
}