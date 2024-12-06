package com.example.demo.controller;

import com.example.demo.model.SuccessStory;
import com.example.demo.service.SuccessStoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SuccessStoryController {
    private final SuccessStoryService successStoryService;

    public SuccessStoryController(SuccessStoryService successStoryService) {
        this.successStoryService = successStoryService;
    }

    @GetMapping("/submitstory")
    public String showSubmitStoryPage(Model model) {
        model.addAttribute("approvedStories", successStoryService.getApprovedStories());
        model.addAttribute("pendingStories", successStoryService.getPendingStories());
        return "submitstory";
    }

    @PostMapping("/api/submit-story")
    public ResponseEntity<String> submitStory(
        @RequestParam(value = "story", required = false) String story,
        @RequestParam(value = "photo", required = false) MultipartFile photo) {
        if (story == null || story.isEmpty() || photo == null || photo.isEmpty()) {
        return ResponseEntity.badRequest().body("Error submitting story: Required parameters are missing");
        }
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

    @PostMapping("/api/approve-story/{id}")
    public ResponseEntity<String> approveStory(@PathVariable Long id) {
        try {
            successStoryService.approveStory(id);
            return ResponseEntity.ok("Story approved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving story: " + e.getMessage());
        }
    }

    @PostMapping("/api/reject-story/{id}")
    public ResponseEntity<String> rejectStory(@PathVariable Long id) {
        try {
            successStoryService.rejectStory(id);
            return ResponseEntity.ok("Story rejected successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error rejecting story: " + e.getMessage());
        }
    }

    @GetMapping("/api/story/{id}")
    public ResponseEntity<SuccessStory> getStory(@PathVariable Long id) {
        try {
            SuccessStory story = successStoryService.getStoryById(id);
            return ResponseEntity.ok(story);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/api/edit-story/{id}")
    public ResponseEntity<String> editStory(
            @PathVariable Long id,
            @RequestParam("story") String storyText) {
        try {
            successStoryService.updateStory(id, storyText);
            return ResponseEntity.ok("Story updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating story: " + e.getMessage());
        }
    }
} 