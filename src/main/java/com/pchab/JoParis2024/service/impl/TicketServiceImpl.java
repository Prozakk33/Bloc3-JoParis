package com.pchab.JoParis2024.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.security.jwt.JwtUtils;
import com.pchab.JoParis2024.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public List<Ticket> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findTicketById(id);
    }

    @Override
    public void createTicket(Ticket ticket) {
        //ticket.setUser(userRepository.findById(ticket.getUser().getId()).orElse(null));
        //ticket.setEvent(eventRepository.findById(ticket.getEvent().getId()).orElse(null));
        ticket.setBuyDate(Timestamp.valueOf(java.time.LocalDateTime.now()));

        // Récupération de la clé Utilisateur
        String userKey = ticket.getUser().getUserKey();
        // Décodage de la clé pour récupérer les informations nécessaires
        String userFirstName = jwtUtils.getFirstNameFromJwtToken(userKey);
        String userLastName = jwtUtils.getLastNameFromJwtToken(userKey);
        // Génération de la clé unique pour le ticket
        String ticketKey = jwtUtils.generateTicketKeyToken(ticket.getUser().getId(), ticket.getEvent().getId(), userFirstName, userLastName, ticket.getBuyDate());
        // Affectation de la clé au ticket
        ticket.setTicketKey(ticketKey);

        ticketRepository.save(ticket);
    }
}
