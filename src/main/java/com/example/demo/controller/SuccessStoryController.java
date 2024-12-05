package com.example.demo.controller;

import com.example.demo.model.SuccessStory;
import com.example.demo.service.SuccessStoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SuccessStoryController {
    private final SuccessStoryService successStoryService;

    public SuccessStoryController(SuccessStoryService successStoryService) {
        this.successStoryService = successStoryService;
    }

    @PostMapping("/api/submit-story")
    public ResponseEntity<String> submitStory(
            @RequestParam("story") String story,
            @RequestParam("photo") MultipartFile photo) {
        
        try {
            SuccessStory successStory = new SuccessStory();
            successStory.setStory(story);
            successStory.setApproved(false);
            
            successStoryService.saveStory(successStory, photo);
            return ResponseEntity.ok("Story submitted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error submitting story: " + e.getMessage());
        }
    }
} 