package com.example.demo.dto;

import lombok.Data;

@Data
public class TwoFactorAuthRequest {
    private String userId;
    private String code;

    // Default constructor
    public TwoFactorAuthRequest() {}

    // Constructor with parameters
    public TwoFactorAuthRequest(String userId, String code) {
        this.userId = userId;
        this.code = code;
    }
} 