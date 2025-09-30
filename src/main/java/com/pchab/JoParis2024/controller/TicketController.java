package com.pchab.JoParis2024.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.pojo.Event;
import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name="Ticket", description="Endpoints for managing tickets")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    public void createTicket(Long userId, Long eventId, String ticketType, Timestamp timestamp) {
        // Implementation for creating a ticket
        User user = userService.findUserById(userId);
        Event event = eventService.findEventById(eventId);

        if (user == null || event == null) {
            throw new IllegalArgumentException("Invalid user or event ID");
        }

        Ticket ticket = new Ticket();
        ticket.setBuyDate(timestamp);
        ticket.setUser(user);
        ticket.setEvent(event);
        ticket.setTicketType(ticketType);
        ticketService.createTicket(ticket);
    }
    
}
