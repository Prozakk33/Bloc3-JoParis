package com.pchab.JoParis2024.security.payload.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime eventDate;
    String eventStadium;
    Long totalTicketsSold;
    Float totalRevenue;
    
}
