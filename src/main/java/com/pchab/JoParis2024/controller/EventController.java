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

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.service.EventService;

@RestController
@RequestMapping("api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/all")
    List<Event> findAllEvent() {
        return eventService.findAllEvent();
    }

    @PostMapping
    void createEvent(@RequestBody Event event) {
        eventService.createEvent(event);
    }

    @PutMapping("/{id}")
    void updateEvent(@RequestBody Event newEvent, @PathVariable Long id) {
        eventService.updateEvent(newEvent, id);
    }

    @GetMapping("/{id}")
    Event findEventById(@PathVariable Long id) {
        return eventService.findEventById(id);
    }
}
