package com.pchab.JoParis2024.service;

import java.util.List;

import com.pchab.JoParis2024.pojo.Ticket;


public interface TicketService {

    List<Ticket> getTicketsByUserId(Long userId);

    List<Ticket> getTicketsByEventId(Long eventId);

    Ticket getTicketById(Long id);

    void createTicket(Ticket ticket);
}
