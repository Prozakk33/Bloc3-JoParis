package com.pchab.JoParis2024.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pchab.JoParis2024.pojo.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByEventId(Long eventId);

    Ticket findTicketById(Long id);

    Ticket findTicketByTicketKey(String ticketKey);
    
}
