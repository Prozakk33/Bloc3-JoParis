package com.pchab.JoParis2024.security.payload.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.pchab.JoParis2024.pojo.Ticket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QRCodeResponse {
    private String qrCodeImageUrl;
    private String userFirstName;
    private String userLastName;
    private String eventTitle;
    private LocalDateTime eventDate;
    private String eventCity;
    private String eventStadium;
    private String ticketType;
    private Timestamp ticketBuyDate;

    // You can add a static method to create a QRCodeResponse from relevant entities if needed
    public static QRCodeResponse fromTicket(Ticket ticket, String qrCodeImageUrl) {
        return new QRCodeResponse(
            "generated-qr-code-url", // Placeholder for actual QR code generation logic
            ticket.getUser().getFirstName(),
            ticket.getUser().getLastName(),
            ticket.getEvent().getTitle(),
            ticket.getEvent().getDate(),
            ticket.getEvent().getCity().toString(),
            ticket.getEvent().getStadium(),
            ticket.getTicketType(),
            ticket.getBuyDate()
        );
    }
}
