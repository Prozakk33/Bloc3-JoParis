package com.pchab.JoParis2024.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.Ticket;
import com.pchab.JoParis2024.repository.EventRepository;
import com.pchab.JoParis2024.repository.TicketRepository;
import com.pchab.JoParis2024.repository.UserRepository;
import com.pchab.JoParis2024.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public List<Ticket> getTicketsByEventId(Long eventId) {
        return ticketRepository.findByEventId(eventId);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
}
