package com.pchab.JoParis2024.security.payload.request;
import lombok.Data;

@Data
public class PaymentRequest {

    private String priceId;
    private Long quantity;
    
    private Object cart;

    // Getters and Setters
    public String getPriceId() {
        return priceId;
    }
    public Long getQuantity() {
        return quantity;
    }
    public Object getCart() {
        return cart;
    }
}
