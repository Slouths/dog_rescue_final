package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DonationPageTest {

    @Autowired
    private MockMvc mockMvc;

    // Test Case 1: Predefined Amount Donation
    @Test
    @WithMockUser // Simulate an authenticated user
    void testPredefinedAmountDonation() throws Exception {
        mockMvc.perform(post("/donation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "50")
                .param("cardNumber", "1111111111111111")
                .param("expiryDate", "12/25")
                .param("cvv", "123")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    // Test Case 2: Custom Amount Donation
    @Test
    @WithMockUser // Simulate an authenticated user
    void testCustomAmountDonation() throws Exception {
        mockMvc.perform(post("/donation") 
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "75")
                .param("cardNumber", "1111111111111111")
                .param("expiryDate", "12/25")
                .param("cvv", "123")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    // Test Case 3: Expired Card
    @Test
    @WithMockUser // Simulate an authenticated user
    void testExpiredCard() throws Exception {
        mockMvc.perform(post("/donation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "50")
                .param("cardNumber", "1111111111111111")
                .param("expiryDate", "12/20") // Expired card
                .param("cvv", "123")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }
}
