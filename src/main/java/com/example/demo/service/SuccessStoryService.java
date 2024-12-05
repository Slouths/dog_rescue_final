package com.example.demo.service;

import com.example.demo.model.SuccessStory;
import org.springframework.web.multipart.MultipartFile;

public interface SuccessStoryService {
    void saveStory(SuccessStory story, MultipartFile photo);
} 