package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoDatabaseTests {

    @Autowired
    private UserServiceImpl userService;

    // Test Case 1: Verify User Data Encryption in Storage (Sunny-Day)
    @Test
    void testUserDataEncryption() {
        // Mock user data
        User user = new User();
        user.setUsername("secureUser");
        user.setPassword("plainPassword123");
        user.setRole("ROLE_USER");

        // Simulate saving the user
        User savedUser = userService.save(user);

        // Verify the password is encrypted in storage
        assertNotEquals("plainPassword123", savedUser.getPassword());
        assertTrue(savedUser.getPassword().startsWith("$2a$")); // Assuming BCrypt
    }

    // Test Case 2: Verify Data Retrieval with Decryption (Sunny-Day)
    @Test
    void testUserDataDecryptionForAuthentication() {
        // Mock registration
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

    // Test Case 3: Unauthorized Access to Encrypted Data (Rainy-Day)
    @Test
    void testUnauthorizedAccessToEncryptedData() {
        // Mock user data
        User user = new User();
        user.setUsername("unauthorizedUser");
        user.setPassword("unauthorizedAccess");
        user.setRole("ROLE_USER");

        // Save user
        userService.save(user);

        // Simulate direct unauthorized access attempt
        String rawPasswordFromDb = userService.getRawPasswordFromDb(user.getId());

        // Verify that raw password is not accessible
        assertNull(rawPasswordFromDb); // Expect null because raw password should never be exposed
    }
}
