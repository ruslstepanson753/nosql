package com.javarush.stepanov.publisher.security;

import lombok.Data;

@Data
public class JwtResponse {

    private String access_token;

    public JwtResponse(String access_token) {
        this.access_token = access_token;
    }
}