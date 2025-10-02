package com.pchab.JoParis2024.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/admin")
@Tag(name="Admin", description="Endpoints for admin operations")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/events")
    public ResponseEntity<List<EventAdminResponse>> getAllEvents(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {

        List<EventAdminResponse> events = eventService.getAllEventsAdmin();

        return ResponseEntity.ok(events);
    }    
}
