package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserService interface that handles user-related operations.
 * Manages user creation, retrieval, and authentication.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor injection of required dependencies.
     */
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds a user by their username.
     * @param username The username to search for
     * @return The found user
     * @throws RuntimeException if user is not found
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    /**
     * Saves a user to the database.
     * Encodes the password if it's not already encoded.
     * @param user The user to save
     * @return The saved user
     */
    @Override
    public User save(User user) {
        // Only encode the password if it's not already encoded (doesn't start with BCrypt prefix)
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    /**
     * Checks if a username already exists in the database.
     * @param username The username to check
     * @return true if username exists, false otherwise
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Registers a new user with basic user role.
     * @param user The user to register
     * @return The registered user
     */
    @Override
    public User registerNewUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        // Set default role for new users
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        return save(user);
    }

    /**
     * Authenticates a user by comparing raw password with the stored encoded password.
     * @param username The username of the user
     * @param rawPassword The raw password entered by the user
     * @return true if authentication succeeds, false otherwise
     */
    public boolean authenticate(String username, String rawPassword) {
        User user = findByUsername(username);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * This method is intentionally unsafe and only for testing unauthorized access attempts.
     * In a real system, this would never expose raw password or encrypted password data.
     * @param userId The ID of the user
     * @return null to simulate secured systems where raw data isn't accessible
     */
    public String getRawPasswordFromDb(Long userId) {
        return null; // Intentionally null to demonstrate protection of sensitive data
    }
}
