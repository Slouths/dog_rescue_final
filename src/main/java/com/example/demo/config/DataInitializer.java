package com.example.demo.config;

import com.example.demo.model.Dog;
import com.example.demo.model.User;
import com.example.demo.service.DogService;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private DogService dogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        logger.info("Starting to initialize admin user...");
        
        try {
            // Check if admin already exists
            if (userService.existsByUsername("admin")) {
                logger.info("Admin user already exists, skipping creation");
                return;
            }
            
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            String rawPassword = "adminpassword";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            admin.setPassword(encodedPassword);
            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);
            
            logger.info("Created admin user with username: {}", admin.getUsername());
            logger.info("Raw admin password: {}", rawPassword);
            logger.info("Encoded admin password: {}", encodedPassword);
            
            User savedAdmin = userService.save(admin); // Use save() directly to bypass role override
            logger.info("Successfully saved admin user with ID: {} and role: {}", 
                savedAdmin.getId(), savedAdmin.getRole());
                
        } catch (Exception e) {
            logger.error("Failed to save admin user", e);
        }

        // Add sample dogs
        createSampleDog("Max", "German Shepherd", "Large", 5);
        createSampleDog("Luna", "Chihuahua", "Small", 2);
        // Add more sample dogs as needed
    }

    private void createSampleDog(String name, String breed, String size, int age) {
        Dog dog = new Dog();
        dog.setName(name);
        dog.setBreed(breed);
        dog.setSize(size);
        dog.setAge(age);
        dog.setDescription("A lovely " + breed + " looking for a forever home!");
        dogService.save(dog);
    }
} 