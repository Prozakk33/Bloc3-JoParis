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

@Controller
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /* Event detail */
    @GetMapping("/id/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventService.findEventById(id));
        return "eventDetail";
    }    

    @PostMapping
    void createEvent(@RequestBody Event event) {
        eventService.createEvent(event);
    }

    @PutMapping("/update/{id}")
    void updateEvent(@RequestBody Event newEvent, @PathVariable Long id) {
        eventService.updateEvent(newEvent, id);
    }

    @GetMapping("/find/{id}")
    Event findEventById(@PathVariable Long id) {
        return eventService.findEventById(id);
    }

    /* List of all events */
    @GetMapping("/all")
    public String allEvents(Model model) {
        model.addAttribute("events", eventService.findAllEvent());
        return "allEvents";  
    }

    @GetMapping("/three")
    public List<Event> threeEvents(Model model) {
        model.addAttribute("events", eventService.findThreeEvents());
        return eventService.findThreeEvents();
    }
}
