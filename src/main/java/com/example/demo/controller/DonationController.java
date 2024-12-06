package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donation")
public class DonationController {

    @PostMapping
    public ResponseEntity<String> processDonation(
            @RequestParam String amount,
            @RequestParam String cardNumber,
            @RequestParam String expiryDate,
            @RequestParam String cvv) {

        // Validate input fields
        if (amount == null || amount.isEmpty() || Double.parseDouble(amount) <= 0) {
            return ResponseEntity.badRequest().body("Invalid donation amount");
        }
        if (!isCardNumberValid(cardNumber)) {
            return ResponseEntity.badRequest().body("Invalid card number");
        }
        if (isCardExpired(expiryDate)) {
            return ResponseEntity.badRequest().body("Card is expired");
        }

        // Process the donation
        return ResponseEntity.ok("Donation processed successfully");
    }

    private boolean isCardNumberValid(String cardNumber) {
        // Basic validation for card number length
        return cardNumber.matches("\\d{16}");
    }

    private boolean isCardExpired(String expiryDate) {
        // Implement logic to validate expiry date (MM/YY format)
        // This is a basic example and should be enhanced for real-world applications
        String[] parts = expiryDate.split("/");
        if (parts.length != 2) {
            return true; // Invalid format
        }
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000;

        java.time.YearMonth currentYearMonth = java.time.YearMonth.now();
        java.time.YearMonth cardYearMonth = java.time.YearMonth.of(year, month);

        return cardYearMonth.isBefore(currentYearMonth);
    }
}
