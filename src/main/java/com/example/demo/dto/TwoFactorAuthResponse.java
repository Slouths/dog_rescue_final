package com.example.demo.dto;

import lombok.Data;

@Data
public class TwoFactorAuthResponse {
    private String message;
    private String code;
    private boolean success;

    // Default constructor
    public TwoFactorAuthResponse() {}

    // Constructor with parameters
    public TwoFactorAuthResponse(String message, String code, boolean success) {
        this.message = message;
        this.code = code;
        this.success = success;
    }
} 