package com.pchab.JoParis2024.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;

@Service
public interface EventService {

    // Find Event
    Event findEventById(Long id);

    // All Event
    List<Event> findAllEvents();

    // Update Event
    void updateEvent(Event event, Long id);

    // Delete Event
    void deleteEventById(Long id);

    //Create Event
    void createEvent(Event event);

    List<Event> findThreeEvents();

    List<EventAdminResponse> getAllEventsAdmin();

    List<String> getAllEventSports();

    List<String> getAllEventsCities();

}
