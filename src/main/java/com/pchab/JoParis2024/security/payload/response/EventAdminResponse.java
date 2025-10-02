package com.pchab.JoParis2024.security.payload.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventAdminResponse {

    Long eventId;
    String eventTitle;
    String eventSport;
    String eventCity;
    LocalDateTime eventDate;
    String eventStadium;
    Long totalTicketsSold;
    Float totalRevenue;
    
}
