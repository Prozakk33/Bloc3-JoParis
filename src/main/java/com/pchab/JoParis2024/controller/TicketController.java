package com.pchab.JoParis2024.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.pojo.User;
import com.pchab.JoParis2024.security.payload.response.QRCodeResponse;
import com.pchab.JoParis2024.security.payload.response.TicketListResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;
import com.pchab.JoParis2024.security.payload.request.QRCodeRequest;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation; 



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
    @Operation(summary = "List tickets for the authenticated user", description = "Returns a list of tickets associated with the authenticated user.")
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

    @PostMapping("/QRCode")
    @Operation(summary = "Generate QR code for a ticket", description = "Generates a QR code for the specified ticket ID.")
    public QRCodeResponse generateQRCode(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @RequestBody QRCodeRequest qrCodeRequest) {
        //TODO: process POST request
        System.out.println("TICKET CONTROLLER - Generating QR code for ticket ID: " + qrCodeRequest.getTicketId() + " with token: " + authorizationHeader);
        Ticket ticket = ticketService.getTicketById(qrCodeRequest.getTicketId());

        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID");
        }
        try {
        BufferedImage qrCodeImage = ticketService.generateQRCodeImage(qrCodeRequest.getTicketId());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", baos);
        byte[] qrCodeBytes = baos.toByteArray();

        return QRCodeResponse()
            .header("Content-Type", "image/png")
            .header("Content-Disposition", "inline; filename=\"qrcode.png\"")
            .body(qrCodeBytes);
        } catch (Exception e) {
            System.err.println("TICKET CONTROLLER - Error generating QR code for ticket ID: " + qrCodeRequest.getTicketId() + " - " + e.getMessage());
            return QRCodeResponse   
                    .body(null);
        }
    }
    
}
