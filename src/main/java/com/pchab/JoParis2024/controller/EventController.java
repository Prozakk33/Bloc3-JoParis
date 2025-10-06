package com.pchab.JoParis2024.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name="Event", description="Endpoints for managing events")
@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /* Event detail */
    @Operation(summary = "Get event details", description = "Retrieve detailed information about a specific event by its ID")
    @GetMapping("/{id}")
    public Event eventDetail(@PathVariable Long id) {
        return eventService.findEventById(id);
    }

    @Operation(summary = "Create event", description = "Create a new event")
    @PostMapping
    public Event createEvent(@Valid @RequestBody Event event) {
        return eventService.createEvent(event);
    }

    @Operation(summary = "Update event", description = "Update an existing event by its ID")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event newEvent, @PathVariable Long id) {
        Event updatedEvent = eventService.updateEvent(newEvent, id);
        return updatedEvent != null ? ResponseEntity.ok(updatedEvent) : ResponseEntity.notFound().build();
    }

    /* List of all events */
    @Operation(summary = "List all events", description = "Retrieve a list of all events")
    @GetMapping("/all")
    public List<Event> allEvents() {
        return eventService.findAllEvents();
    }

    @Operation(summary = "List three events", description = "Retrieve a list of three events")
    @GetMapping("/three")
    public List<Event> threeEvents() {
        return eventService.findThreeEvents();
    }

    @GetMapping("/sportList")
    @Operation(summary = "List all sports", description = "Retrieve a list of all sports for the events")   
    public List<String> sportList() {
        return eventService.getAllEventSports();
    }

    @GetMapping("/cityList")
    @Operation(summary = "List all cities", description = "Retrieve a list of all cities where events are held")
    public List<String> cityList() {
        return eventService.getAllEventsCities();
    }  
}