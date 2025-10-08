package com.pchab.JoParis2024.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.CityEnum;
import com.pchab.JoParis2024.pojo.Event;
import com.pchab.JoParis2024.pojo.SportEnum;
import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.security.payload.response.EventAdminResponse;
import com.pchab.JoParis2024.service.EventService;
import com.pchab.JoParis2024.service.TicketService;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketService ticketService;

    @Override
    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

     
    @Override
    public void deleteEventById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event updateEvent(Event event, Long id) {
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

            return eventRepository.save(oldEvent);
        } else {
            return null;
        }
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<Event> findThreeEvents() {
        return eventRepository.findTop3ByOrderByDateAsc();
    }

    @Override
    public List<EventAdminResponse> getAllEventsAdmin() {
        /* Récupération de tous les évènements */
        List<Event> eventsList = eventRepository.findAll();

        System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Fetched events: " + eventsList.size());

        /* Création de la liste des réponses */
        List<EventAdminResponse> adminResponseList = new ArrayList<>();

        /* Pour chaque évènement, on crée une réponse et on l'ajoute à la liste */
        for (Event event : eventsList) {
            EventAdminResponse response = new EventAdminResponse();
            response.setEventId(event.getId());
            response.setEventTitle(event.getTitle());
            response.setEventSport(event.getSport().toString());
            response.setEventCity(event.getCity().toString());
            response.setEventDate(event.getDate());
            response.setEventStadium(event.getStadium());

            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Response: " + response.getEventId() + " - " + event.getTitle());
            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Response Title: " + response.getEventTitle());
            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Response City: " + response.getEventCity());
            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Response Sport: " + response.getEventSport());
            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Response Date: " + response.getEventDate());
            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Response Stadium: " + response.getEventStadium());

            /* Récupération des informations des billets */
            List<Ticket> tickets = ticketService.getTicketsByEventId(event.getId());

            System.out.println("-- EventServiceImpl - GetAllEventsAdmin - Fetched tickets for event " + event.getId() + ": " + tickets.size());

            Long totalTicketsSold = 0L;
            Float totalRevenue = 0.0f;
            if (tickets != null) {
                for (Ticket ticket : tickets) {
                    switch (ticket.getTicketType()) {
                        case "Solo":
                            totalTicketsSold += 1;
                            totalRevenue += event.getPrice();
                            break;
                        case "Duo":
                            totalTicketsSold += 2;
                            totalRevenue += event.getPrice() * 2 * 0.9f;
                            break;
                        case "Famille":
                            totalTicketsSold += 4;
                            totalRevenue += event.getPrice() * 4 * 0.8f;
                            break;
                    }
                }
            }
            response.setTotalTicketsSold(totalTicketsSold);
            response.setTotalRevenue(totalRevenue);
            adminResponseList.add(response);           
        }

        return adminResponseList;
    }

    @Override
    public List<String> getAllEventSports() {
        return Arrays.stream(SportEnum.values())
                 .map(Enum::name) // Récupérer les noms des valeurs de l'énumération
                 .toList();
    }

    @Override
    public List<String> getAllEventsCities() {
        return Arrays.stream(CityEnum.values())
                 .map(Enum::name) // Récupérer les noms des valeurs de l'énumération
                 .toList();
    }

}
