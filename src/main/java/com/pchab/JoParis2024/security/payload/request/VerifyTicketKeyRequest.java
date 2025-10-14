package com.pchab.JoParis2024.security.payload.request;

import lombok.Data;

@Data
public class VerifyTicketKeyRequest {
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
