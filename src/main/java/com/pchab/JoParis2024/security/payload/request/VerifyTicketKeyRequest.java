package com.pchab.JoParis2024.security.payload.request;

import lombok.Data;

@Data
public class VerifyTicketKeyRequest {
    private String ticketKey;

    public String getTicketKey() {
        return ticketKey;
    }

    public void setTicketKey(String ticketKey) {
        this.ticketKey = ticketKey;
    }
}
