package com.pchab.JoParis2024.service;

import java.util.List;

import com.pchab.JoParis2024.pojo.Event;

public interface EventService {

    // Find Event
    Event findEventById(Long id);

    // All Event
    List<Event> findAllEvent();

    // Update Event
    void updateEventById(Event event, Long id);

    // Delete Event
    void deleteEventById(Long id);

    //Create Event
    void createEvent(Event event);

}
