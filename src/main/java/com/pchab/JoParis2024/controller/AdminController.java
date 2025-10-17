package com.pchab.JoParis2024.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.security.payload.request.NewEventRequest;
import com.pchab.JoParis2024.security.payload.request.PutEventRequest;
import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/admin")
@Tag(name="Admin", description="Endpoints for admin operations")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/events")
    @Operation(summary = "Get all events (admin)", description = "Retrieve a list of all events with admin details")
    public ResponseEntity<List<EventAdminResponse>> getAllEvents(@RequestHeader(value = "Authorization", required = true) String authorizationHeader) {

        List<EventAdminResponse> events = eventService.getAllEventsAdmin();
       
        return ResponseEntity.ok(events);
    }

    @PostMapping("/createEvent")
    @Operation(summary = "Create a new event", description = "Create a new event with the provided details")
    public ResponseEntity<String> createEvent(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,@Valid @RequestBody NewEventRequest newEventRequest) {

        Event event = new Event();
        event.setTitle(newEventRequest.getEventTitle());
        event.setDescription(newEventRequest.getEventDescription());
        event.setDate(newEventRequest.getEventDate());
        event.setSport(newEventRequest.getEventSport());
        event.setCity(newEventRequest.getEventCity());
        event.setCapacity(newEventRequest.getEventCapacity());
        event.setPrice(newEventRequest.getEventPrice());
        event.setStadium(newEventRequest.getEventStadium());

        System.out.println("---- ***************** Creating new event ----");
/*
        if (newEventRequest == null) {
            System.out.println("NewEventRequest is null!");
        } else {
            System.out.println("NewEventRequest is not null.");
        }

        System.out.println("Received new event creation request: " + newEventRequest);
        System.out.println("Title: " + newEventRequest.getEventTitle());
        System.out.println("Description: " + newEventRequest.getEventDescription());
        System.out.println("Date: " + newEventRequest.getEventDate());
        System.out.println("Sport: " + newEventRequest.getEventSport());
        System.out.println("City: " + newEventRequest.getEventCity());
        System.out.println("Capacity: " + newEventRequest.getEventCapacity());
        System.out.println("Price: " + newEventRequest.getEventPrice());
        System.out.println("Stadium: " + newEventRequest.getEventStadium());
*/

        Event createdEvent = eventService.createEvent(event);

        //System.out.println("--- Event created: " + createdEvent);

        return ResponseEntity.ok("Event created successfully with ID: " + createdEvent.getId());
    }

    @PutMapping("/updateEvent")
    @Operation(summary = "Update an existing event", description = "Update an existing event with the provided details")
    public ResponseEntity<?> updateEvent(@RequestHeader(value = "Authorization", required = true) String authorizationHeader,@Valid @RequestBody PutEventRequest putEventRequest) {
        
        /*
        System.out.println("Received event update request: " + putEventRequest);
        System.out.println("ID: " + putEventRequest.getEventId());
        System.out.println("Title: " + putEventRequest.getEventTitle());
        System.out.println("Description: " + putEventRequest.getEventDescription());
        System.out.println("Date: " + putEventRequest.getEventDate());
        System.out.println("Sport: " + putEventRequest.getEventSport());
        System.out.println("City: " + putEventRequest.getEventCity());
        System.out.println("Capacity: " + putEventRequest.getEventCapacity());
        System.out.println("Price: " + putEventRequest.getEventPrice());
        System.out.println("Stadium: " + putEventRequest.getEventStadium());
     */   
        Event event = new Event();
        event.setId(putEventRequest.getEventId());
        event.setTitle(putEventRequest.getEventTitle());
        event.setDescription(putEventRequest.getEventDescription());
        event.setDate(putEventRequest.getEventDate());
        event.setSport(putEventRequest.getEventSport());
        event.setCity(putEventRequest.getEventCity());
        event.setCapacity(putEventRequest.getEventCapacity());
        event.setPrice(putEventRequest.getEventPrice());
        event.setStadium(putEventRequest.getEventStadium());

        /* Sauvegarde des modifications */
        Event updatedEvent = eventService.updateEvent(event, event.getId());
        //System.out.println("--- Event updated: " + updatedEvent);
        return ResponseEntity.ok().body("Epreuve mise à jour avec succès !");
    }
}
