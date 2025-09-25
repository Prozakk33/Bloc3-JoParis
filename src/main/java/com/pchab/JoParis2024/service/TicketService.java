package com.pchab.JoParis2024.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pchab.JoParis2024.pojo.Ticket;

@Service
public interface TicketService {

    List<Ticket> getTicketsByUserId(Long userId);

    List<Ticket> getTicketsByEventId(Long eventId);

    Ticket getTicketById(Long id);  
}
