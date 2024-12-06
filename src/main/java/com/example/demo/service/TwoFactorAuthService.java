package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class TwoFactorAuthService {
    private final Map<String, String> userCodes = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String generateCode(String userId) {
        // Generate a 6-digit code
        String code = String.format("%06d", random.nextInt(1000000));
        userCodes.put(userId, code);
        return code;
    }

    public boolean verifyCode(String userId, String code) {
        String storedCode = userCodes.get(userId);
        if (storedCode != null && storedCode.equals(code)) {
            userCodes.remove(userId); // Remove code after successful verification
            return true;
        }
        return false;
    }
} 