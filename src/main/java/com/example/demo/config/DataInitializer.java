package com.example.demo.config;

import com.example.demo.model.Dog;
import com.example.demo.model.User;
import com.example.demo.service.DogService;
import com.example.demo.service.UserService;
import com.example.demo.service.SuccessStoryService;
import com.example.demo.model.SuccessStory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private DogService dogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SuccessStoryService successStoryService;

    // Lists of sample data
    private final List<String> dogNames = Arrays.asList(
        "Max", "Luna", "Charlie", "Bella", "Rocky", "Lucy", "Cooper", "Daisy", 
        "Milo", "Molly", "Buddy", "Bailey", "Jack", "Sadie", "Duke", "Lily",
        "Oliver", "Sophie", "Tucker", "Ruby", "Bear", "Penny", "Zeus", "Rosie", "Leo"
    );

    private final List<String> breeds = Arrays.asList(
        "German Shepherd", "Labrador", "Golden Retriever", "Bulldog", "Poodle",
        "Beagle", "Rottweiler", "Yorkshire Terrier", "Boxer", "Dachshund"
    );

    private final List<String> sizes = Arrays.asList("Small", "Medium", "Large");

    @Override
    public void run(String... args) {
        initializeAdmin();
        initializeDogs();
        initializeSuccessStories();
    }

    private void initializeAdmin() {
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
    }

    private void initializeDogs() {
        // Create 25 dogs
        for (int i = 0; i < 25; i++) {
            Dog dog = new Dog();
            dog.setName(dogNames.get(i));
            dog.setBreed(breeds.get(i % breeds.size()));
            dog.setSize(sizes.get(i % sizes.size()));
            dog.setAge(1 + (int)(Math.random() * 12)); // Random age between 1-12
            dog.setDescription("A lovely " + breeds.get(i % breeds.size()) + 
                " who loves " + (i % 2 == 0 ? "playing fetch" : "going for walks") + 
                " and is " + (i % 2 == 0 ? "great with kids" : "perfect for active families") + "!");
            dog.setImageUrl("/images/dog" + (i % 5 + 1) + ".jpg"); // Cycling through 5 sample images
            
            // Mark first 5 dogs as featured
            if (i < 5) {
                dog.setFeatured(true);
            }
            
            dogService.save(dog);
        }
    }

    private void initializeSuccessStories() {
        String[] stories = {
            "We adopted Max last year and he has brought so much joy to our family. He loves playing with our kids and going for long walks in the park.",
            "Luna was shy when we first got her, but now she's the most confident and loving dog. She's completely transformed our lives!",
            "Charlie was a senior dog when we adopted him, but he still has so much love to give. He's the perfect companion for our quiet lifestyle.",
            "Bella has become best friends with our cat and they're inseparable. We couldn't have asked for a better addition to our family."
        };

        for (int i = 0; i < stories.length; i++) {
            SuccessStory story = new SuccessStory();
            story.setStory(stories[i]);
            story.setApproved(true);
            successStoryService.saveInitialStory(story, "sample-success-" + (i + 1) + ".jpg");
        }
    }
} 