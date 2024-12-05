package com.example.demo.service;

import com.example.demo.model.SuccessStory;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface SuccessStoryService {
    void saveStory(SuccessStory story, MultipartFile photo);
    List<SuccessStory> getApprovedStories();
    List<SuccessStory> getPendingStories();
    void approveStory(Long id);
    void rejectStory(Long id);
    void saveInitialStory(SuccessStory story, String photoFileName);
    SuccessStory getStoryById(Long id);
    void updateStory(Long id, String story);
} 