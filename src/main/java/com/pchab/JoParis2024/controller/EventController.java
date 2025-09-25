package com.pchab.JoParis2024.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.service.EventService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@Tag(name="Event", description="Endpoints for managing events")
@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /* Event detail */
    @Operation(summary = "Get event details", description = "Retrieve detailed information about a specific event by its ID")
    @GetMapping("/id/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findEventById(id));
        return "eventDetail";
    }    

    @Operation(summary = "Create event", description = "Create a new event")
    @PostMapping
    void createEvent(@RequestBody Event event) {
        eventService.createEvent(event);
    }

    @Operation(summary = "Update event", description = "Update an existing event by its ID")
    @PutMapping("/update/{id}")
    void updateEvent(@RequestBody Event newEvent, @PathVariable Long id) {
        eventService.updateEvent(newEvent, id);
    }

    @Operation(summary = "Delete event", description = "Delete an event by its ID")
    @GetMapping("/find/{id}")
    Event findEventById(@PathVariable Long id) {
        return eventService.findEventById(id);
    }

    /* List of all events */
    @Operation(summary = "List all events", description = "Retrieve a list of all events")
    @GetMapping("/all")
    public String allEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "allEvents";  
    }

    @Operation(summary = "List three events", description = "Retrieve a list of three events")
    @GetMapping("/three")
    public List<Event> threeEvents(Model model) {
        model.addAttribute("events", eventService.findThreeEvents());
        return eventService.findThreeEvents();
    }
}
