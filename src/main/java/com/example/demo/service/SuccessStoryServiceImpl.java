package com.example.demo.service;

import com.example.demo.model.SuccessStory;
import com.example.demo.repository.SuccessStoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;

@Service
public class SuccessStoryServiceImpl implements SuccessStoryService {
    private final SuccessStoryRepository successStoryRepository;
    private final String uploadDir = "uploads/stories";

    public SuccessStoryServiceImpl(SuccessStoryRepository successStoryRepository) {
        this.successStoryRepository = successStoryRepository;
    }

    @Override
    public void saveStory(SuccessStory story, MultipartFile photo) {
        try {
            // Create uploads directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            
            // Save the file
            Files.copy(photo.getInputStream(), filePath);
            
            // Set the photo path in the story
            story.setPhotoPath(filename);
            
            // Save the story to database
            successStoryRepository.save(story);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public void saveInitialStory(SuccessStory story, String photoFileName) {
        story.setPhotoPath(photoFileName);
        successStoryRepository.save(story);
    }

    @Override
    public List<SuccessStory> getApprovedStories() {
        return successStoryRepository.findByApprovedTrueOrderByCreatedAtDesc();
    }

    @Override
    public List<SuccessStory> getPendingStories() {
        return successStoryRepository.findByApprovedFalseOrderByCreatedAtDesc();
    }

    @Override
    public void approveStory(Long id) {
        SuccessStory story = successStoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Story not found"));
        story.setApproved(true);
        successStoryRepository.save(story);
    }

    @Override
    public void rejectStory(Long id) {
        successStoryRepository.deleteById(id);
    }

    @Override
    public SuccessStory getStoryById(Long id) {
        return successStoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Story not found"));
    }

    @Override
    public void updateStory(Long id, String storyText) {
        SuccessStory story = getStoryById(id);
        story.setStory(storyText);
        successStoryRepository.save(story);
    }
} 