package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.TwoFactorAuthService;
import com.example.demo.dto.TwoFactorAuthRequest;
import com.example.demo.dto.TwoFactorAuthResponse;

@RestController
@RequestMapping("/api/2fa")
public class TwoFactorAuthController {
    
    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @PostMapping("/generate")
    public ResponseEntity<TwoFactorAuthResponse> generateCode(@RequestBody TwoFactorAuthRequest request) {
        String code = twoFactorAuthService.generateCode(request.getUserId());
        TwoFactorAuthResponse response = new TwoFactorAuthResponse(
            "2FA code generated successfully",
            code,
            true
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<TwoFactorAuthResponse> verifyCode(@RequestBody TwoFactorAuthRequest request) {
        boolean isValid = twoFactorAuthService.verifyCode(request.getUserId(), request.getCode());
        if (isValid) {
            return ResponseEntity.ok(new TwoFactorAuthResponse(
                "Code verified successfully",
                null,
                true
            ));
        }
        return ResponseEntity.badRequest().body(new TwoFactorAuthResponse(
            "Invalid code",
            null,
            false
        ));
    }
} 