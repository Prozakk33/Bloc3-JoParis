package com.pchab.JoParis2024.security.payload.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DecodeQRCodeResponse {

    String userFirstName;
    String userLastName;
    Timestamp ticketBuyDate;
    String eventTitle;
    String eventCity;
    LocalDateTime eventDate;
    String ticketType;
/*
    public static DecodeQRCodeResponse fromTicketToken() {

    }
    */
    
}
