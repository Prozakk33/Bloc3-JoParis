package com.pchab.JoParis2024.security.payload.request;

import lombok.Data;


@Data
public class AccessRequest {

    private String token;

    public String getToken() {
        return token;
    }

}
