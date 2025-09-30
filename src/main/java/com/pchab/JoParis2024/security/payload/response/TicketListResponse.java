package com.pchab.JoParis2024.security.payload.response;

import com.pchab.JoParis2024.pojo.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TicketListResponse {

    private Long ticketId;
    private String eventTitle;
    private String eventSport;
    private LocalDateTime eventDate;
    private String eventCity;
    private String eventStadium;
    private String ticketType;

    public static TicketListResponse fromTicket(Ticket ticket) {
        return new TicketListResponse(
            ticket.getId(),
            ticket.getEvent().getTitle(),
            ticket.getEvent().getSport().toString(),
            ticket.getEvent().getDate(),
            ticket.getEvent().getCity().toString(),
            ticket.getEvent().getStadium(),
            ticket.getTicketType()
        );
    }
}
