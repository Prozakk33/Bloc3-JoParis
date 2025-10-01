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


    // You can add a static method to create a QRCodeResponse from relevant entities if needed
    public static QRCodeResponse fromTicket(String qrCodeImageUrl) {
        return new QRCodeResponse(qrCodeImageUrl);
    }
}
