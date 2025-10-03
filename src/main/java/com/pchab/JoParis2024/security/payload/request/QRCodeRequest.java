package com.pchab.JoParis2024.security.payload.request;

import lombok.Data;

@Data
public class QRCodeRequest {

    private Long ticketId;

    public Long getTicketId() {
        return ticketId;
    }
    
}
