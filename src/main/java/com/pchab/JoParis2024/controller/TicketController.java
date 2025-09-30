package com.pchab.JoParis2024.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.service.UserService;
import com.pchab.JoParis2024.service.EventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.pojo.Event;
import java.util.List;
import java.sql.Timestamp;
import com.pchab.JoParis2024.security.payload.response.TicketListResponse;

import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name="Ticket", description="Endpoints for managing tickets")
@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserController userController;

    @Autowired
    private AuthController authController;

    @Autowired
    private EventService eventService;

    public void createTicket(Long userId, Long eventId, String ticketType, Timestamp timestamp) {
        // Implementation for creating a ticket
        User user = userController.findUserById(userId);
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

    @PostMapping("/list")
    public List<TicketListResponse> listTickets(@RequestHeader (value = "Authorization", required = true) String authorizationHeader) {

        System.out.println("TICKET CONTROLLER - Listing tickets with token: " + authorizationHeader);  
        ResponseEntity<?> responseUser = userController.getUserFromToken(authorizationHeader);
        User user = (User) responseUser.getBody();
        System.out.println("TICKET CONTROLLER - Retrieved user from token");

        // Implementation for listing tickets
        if (user == null) {
            System.err.println("TICKET CONTROLLER - No user found from token: " + authorizationHeader);
            throw new IllegalArgumentException("Invalid user");
        }
        Long userId = user.getId();
        System.out.println("TICKET CONTROLLER - Listing tickets for user ID: " + userId);
        List<Ticket> tickets = ticketService.getTicketsByUserId(userId);
        List<TicketListResponse> ticketList = new java.util.ArrayList<>();
        for (Ticket ticket : tickets) {
            System.out.println("TICKET CONTROLLER - Ticket ID: " + ticket.getId() + ", Event: " + ticket.getEvent().getTitle() + ", Type: " + ticket.getTicketType());
            TicketListResponse responseTicket = TicketListResponse.fromTicket(ticket);
            ticketList.add(responseTicket);
        }
        System.out.println("TICKET CONTROLLER - List of tickets for user ID: " + userId + " - " + ticketList.toString());
        return ticketList;
    }
}
