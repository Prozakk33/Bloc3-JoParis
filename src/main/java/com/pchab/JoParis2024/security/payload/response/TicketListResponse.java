package com.pchab.JoParis2024.security.payload.response;

import java.time.LocalDateTime;

import com.pchab.JoParis2024.pojo.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
