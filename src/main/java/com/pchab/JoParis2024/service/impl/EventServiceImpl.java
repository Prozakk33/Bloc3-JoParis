package com.pchab.JoParis2024.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.service.EventService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event findEventById(Long event_id) {
        return eventRepository.findById(event_id).orElse(null);
    }

    @Override
    public void deleteEventById(Long event_id) {
        eventRepository.deleteById(event_id);
    }

    @Override
    public void updateEventById(Event event, Long event_id) {
        Event oldEvent = this.findEventById(event_id);

        if (oldEvent != null) {
//		oldEvent.setDescription(event.getDescription());
//			oldEvent.setTitle(event.getTitle());
        }

        eventRepository.save(oldEvent);

    }

    @Override
    public void createEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public List<Event> findAllEvent() {
        return eventRepository.findAll();
    }

}
