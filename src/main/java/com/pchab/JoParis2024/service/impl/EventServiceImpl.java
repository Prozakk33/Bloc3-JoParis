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
    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEventById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public void updateEvent(Event event, Long id) {
        Event oldEvent = this.findEventById(id);

        if (oldEvent != null) {
	    	oldEvent.setDescription(event.getDescription());
            oldEvent.setTitle(event.getTitle());
            oldEvent.setDate(event.getDate());
            oldEvent.setSport(event.getSport());
            oldEvent.setCity(event.getCity());
            oldEvent.setStadium(event.getStadium());
            oldEvent.setCapacity(event.getCapacity());
            oldEvent.setPrice(event.getPrice());

            eventRepository.save(oldEvent);
        }

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
