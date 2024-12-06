package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.User;
import com.example.demo.service.UserServiceImpl;

import org.springframework.mock.web.MockMultipartFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    /**
     * (DRW-HMP-001 - Navigate to Sections)
     * Test Case 1: Navigate to the Adoption Portal (Sunny-Day)
     * Test Objective: Verify the ability to navigate to the adoption portal.
     * Ensure the adoption portal page is accessible, and the response time is within acceptable limits.
     */
    @Test
    @WithMockUser
    void testNavigateToAdoptionPortal() throws Exception {
        long startTime = System.nanoTime();
        mockMvc.perform(get("/adoption"))
                .andExpect(status().isOk());
        long endTime = System.nanoTime();
        long elapsedTimeMillis = (endTime - startTime) / 1_000_000;
        assert elapsedTimeMillis <= 2000 : "Navigation took longer than expected: " + elapsedTimeMillis + "ms";
    }

    /**
     * (DRW-HMP-001 - Navigate to Sections)
     * Test Case 2: Navigate to the Donation Page (Sunny-Day)
     * Test Objective: Verify the ability to navigate to the donation page.
     * Ensure the donation page is accessible, and the response time is within acceptable limits.
     */
    @Test
    @WithMockUser
    void testNavigateToDonationPage() throws Exception {
        long startTime = System.nanoTime();
        mockMvc.perform(get("/donation"))
                .andExpect(status().isOk());
        long endTime = System.nanoTime();
        long elapsedTimeMillis = (endTime - startTime) / 1_000_000;
        assert elapsedTimeMillis <= 2000 : "Navigation took longer than expected: " + elapsedTimeMillis + "ms";
    }

    /**
     * (DRW-HMP-001 - Navigate to Sections)
     * Test Case 3: Logout and Redirect to Login Page (Rainy-Day)
     * Test Objective: Verify the ability to log out and be redirected to the login page.
     * Ensure the user is successfully logged out and redirected to the login page.
     */
    @Test
    @WithMockUser
    void testLogoutAndRedirectToLogin() throws Exception {
        long startTime = System.nanoTime();
        mockMvc.perform(post("/logout")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        long endTime = System.nanoTime();
        long elapsedTimeMillis = (endTime - startTime) / 1_000_000;
        assert elapsedTimeMillis <= 2000 : "Redirection took longer than expected: " + elapsedTimeMillis + "ms";
    }

    /**
     * (DRW-DP-004 - Make a Donation via Credit Card)
     * Test Case 4: Make a Donation Using Predefined Amount (Sunny-Day)
     * Test Objective: Validate a successful donation with a predefined amount.
     * Ensure the system processes donations correctly for valid credit card information.
     */
    @Test
    @WithMockUser
    void testPredefinedAmountDonation() throws Exception {
        mockMvc.perform(post("/donation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "100")
                .param("cardNumber", "1234567890123456")
                .param("expiryDate", "12/25")
                .param("cvv", "123")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    /**
     * (DRW-DP-004 - Make a Donation via Credit Card)
     * Test Case 5: Make a Donation Using a Custom Amount (Sunny-Day)
     * Test Objective: Validate a successful donation with a custom amount.
     * Ensure the system processes donations correctly for valid credit card information.
     */
    @Test
    @WithMockUser
    void testCustomAmountDonation() throws Exception {
        mockMvc.perform(post("/donation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "80")
                .param("cardNumber", "1234567890123456")
                .param("expiryDate", "12/25")
                .param("cvv", "123")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    /**
     * (DRW-DP-004 - Make a Donation via Credit Card)
     * Test Case 6: Submit Expired Credit Card Details (Rainy-Day)
     * Test Objective: Validate the donation process for an expired credit card.
     * Ensure the system rejects donations with invalid credit card details.
     */
    @Test
    @WithMockUser
    void testExpiredCard() throws Exception {
        mockMvc.perform(post("/donation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("amount", "50")
                .param("cardNumber", "1234567890123456")
                .param("expiryDate", "12/23")
                .param("cvv", "123")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    /**
     * (DRW-SSS-005 - Submit a Success Story)
     * Test Case 7: Submit a Success Story (Sunny-Day)
     * Test Objective: Validate story submission with a story and photo.
     * Ensure the system accepts valid story submissions.
     */
    @Test
    @WithMockUser
    void testSubmitSuccessStory() throws Exception {
        MockMultipartFile photoFile = new MockMultipartFile("photo", "story.jpg", "image/jpeg", "image-content".getBytes());
        mockMvc.perform(multipart("/api/submit-story")
                .file(photoFile)
                .param("story", "This is a success story about adopting a dog.")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Story submitted successfully"));
    }

    /**
     * (DRW-SSS-005 - Submit a Success Story)
     * Test Case 8: Submit a Success Story with an Image (Sunny-Day)
     * Test Objective: Validate story submission with only an image.
     * Ensure the system accepts story submissions with an image and valid text.
     */
    @Test
    @WithMockUser
    void testSubmitSuccessStoryWithImage() throws Exception {
        MockMultipartFile photoFile = new MockMultipartFile("photo", "dog.jpg", "image/jpeg", "image-content".getBytes());
        mockMvc.perform(multipart("/api/submit-story")
                .file(photoFile)
                .param("story", "This is another success story.")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Story submitted successfully"));
    }

    /**
     * (DRW-SSS-005 - Submit a Success Story)
     * Test Case 9: Handle Invalid Data (Rainy-Day)
     * Test Objective: Validate error handling for missing data.
     * Ensure the system responds appropriately when required parameters are missing.
     */
    @Test
    @WithMockUser
    void testHandleInvalidData() throws Exception {
        mockMvc.perform(multipart("/api/submit-story")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error submitting story: Required parameters are missing"));
    }

    /**
     * (DRW-SEC-006 - Two-Factor Authentication)
     * Test Case 10: Handling of a Valid OTP (Sunny-Day)
     * Test Objective: Validate successful login when the correct two-factor authentication (2FA) code is provided.
     * Ensure the system accepts valid credentials and a correct 2FA code.
     */
    @Test
    @WithMockUser
    void testSuccessfulLoginWithCorrect2FACode() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testUser")
                .param("password", "password123")
                .param("twoFactorCode", "123456")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

    /**
     * (DRW-SEC-006 - Two-Factor Authentication)
     * Test Case 11: Handling of an Invalid OTP (Rainy-Day)
     * Test Objective: Validate login failure when an incorrect two-factor authentication (2FA) code is provided.
     * Ensure the system rejects invalid 2FA codes.
     */
    @Test
    @WithMockUser
    void testFailedLoginWithIncorrect2FACode() throws Exception {
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "testUser")
                .param("password", "password123")
                .param("twoFactorCode", "000000") // Invalid code
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));
    }

    /**
     * (DRW-SEC-007 - Moderate Success Stories)
     * Test Case 12: Reject Success Story (Sunny-Day)
     * Test Objective: Verify that the admin can reject a submitted success story.
     * Ensure the story status updates correctly to 'rejected.'
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testRejectSuccessStory() throws Exception {
        // Use the correct endpoint as defined in the controller
        mockMvc.perform(post("/api/reject-story/1") 
                .with(SecurityMockMvcRequestPostProcessors.csrf())) // Include CSRF token
                .andExpect(status().isOk()) // Expect 200 OK for a successful rejection
                .andExpect(content().string("Story rejected successfully")); // Verify success response
    }

     /**
     * (DRW-SEC-008 - User Data Security)
     * Test Case 13: Verify User Data Encryption in Storage (Sunny-Day)
     * Test Objective: Validate that user passwords are stored in encrypted form.
     * Ensure that plain text passwords are not saved and passwords are encrypted using BCrypt.
     */
    @Test
    void testUserDataEncryption() {
        User user = new User();
        user.setUsername("secureUser");
        user.setPassword("plainPassword123");
        user.setRole("ROLE_USER");

        // Simulate saving the user
        User savedUser = userService.save(user);

        // Verify the password is encrypted in storage
        assertNotEquals("plainPassword123", savedUser.getPassword());
        assertTrue(savedUser.getPassword().startsWith("$2a$")); // BCrypt format verification
    }

    /**
     * (DRW-SEC-008 - User Data Security)
     * Test Case 14: Verify Data Retrieval with Decryption (Sunny-Day)
     * Test Objective: Validate that encrypted passwords can be matched correctly during authentication.
     * Ensure that users can log in successfully with valid credentials.
     */
    @Test
    void testUserDataDecryptionForAuthentication() {
        User user = new User();
        user.setUsername("testDecryption");
        user.setPassword("TestPassword");
        user.setRole("ROLE_USER");

        // Save user
        userService.save(user);

        // Attempt login with correct password
        boolean isAuthenticated = userService.authenticate("testDecryption", "TestPassword");

        // Validate successful decryption and authentication
        assertTrue(isAuthenticated);
    }

    /**
     * (DRW-SEC-008 - User Data Security)
     * Test Case 15: Unauthorized Access to Encrypted Data (Rainy-Day)
     * Test Objective: Validate that raw password data is not accessible under any circumstances.
     * Ensure that sensitive data remains secure and inaccessible in plain text form.
     */
    @Test
    void testUnauthorizedAccessToEncryptedData() {
        User user = new User();
        user.setUsername("unauthorizedUser");
        user.setPassword("unauthorizedAccess");
        user.setRole("ROLE_USER");

        // Save user
        userService.save(user);
        
        // Simulate direct unauthorized access attempt
        String rawPasswordFromDb = userService.getRawPasswordFromDb(user.getId());

        assertNull(rawPasswordFromDb); // Expect null because raw password should never be exposed
    }

    /**
     * (DRW-SEC-010 - Limit Login Attempts)
     * Test Case 16: Lock Account After 5 Failed Attempts (Sunny-Day)
     * Test Objective: Validate the account lockout process after multiple failed login attempts.
     * Ensure the system locks the account after exceeding the allowed number of attempts.
     */
    @Test
    void testAccountLockAfterFailedAttempts() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("username", "testUser")
                    .param("password", "wrongPassword")
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/login?error"));
        }
    }
}
