package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.model.Dog;
import com.example.demo.service.UserService;
import com.example.demo.service.DogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.http.HttpStatus.FOUND;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    // Test Case 1: User Registration
    @Test
    void testUserRegistration() throws Exception {
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testuser")
                .param("password", "password123"))
                .andExpect(status().is(FOUND.value()))
                .andExpect(redirectedUrl("/login?registered"));
    }

    // Test Case 2: User Login
    @Test
    void testUserLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testuser")
                .param("password", "password123"))
                .andExpect(status().is(FOUND.value()));
    }

    // Test Case 3: Access Protected Page Without Authentication
    @Test
    void testUnauthenticatedAccess() throws Exception {
        mockMvc.perform(get("/adoption"))
                .andExpect(status().is(FOUND.value()))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    // Test Case 4: Access Protected Page With Authentication
    @Test
    @WithMockUser
    void testAuthenticatedAccess() throws Exception {
        mockMvc.perform(get("/adoption"))
                .andExpect(status().isOk())
                .andExpect(view().name("adoption"));
    }

    // Test Case 5: Donation Form Submission
    @Test
    @WithMockUser
    void testDonationSubmission() throws Exception {
        mockMvc.perform(post("/donation/process")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "25")
                .param("cardNumber", "4111111111111111")
                .param("expiryDate", "12/25")
                .param("cvv", "123"))
                .andExpect(status().isOk());
    }

    // Test Case 6: Featured Dogs Display
    @Test
    @WithMockUser
    void testFeaturedDogs() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("featuredDogs"));
    }

    // Test Case 7: Invalid Login Attempt
    @Test
    void testInvalidLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "wronguser")
                .param("password", "wrongpass"))
                .andExpect(status().is(FOUND.value()))
                .andExpect(redirectedUrl("/login?error"));
    }

    // Test Case 8: Duplicate Username Registration
    @Test
    void testDuplicateRegistration() throws Exception {
        // First registration
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "duplicateuser")
                .param("password", "password123"));

        // Second registration with same username
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "duplicateuser")
                .param("password", "password123"))
                .andExpect(model().attributeExists("error"));
    }

    // Test Case 9: Logout Functionality
    @Test
    @WithMockUser
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is(FOUND.value()))
                .andExpect(redirectedUrl("/login?logout"));
    }

    // Test Case 10: Invalid Donation Amount
    @Test
    @WithMockUser
    void testInvalidDonationAmount() throws Exception {
        mockMvc.perform(post("/donation/process")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "-25")
                .param("cardNumber", "4111111111111111")
                .param("expiryDate", "12/25")
                .param("cvv", "123"))
                .andExpect(status().isBadRequest());
    }
}
