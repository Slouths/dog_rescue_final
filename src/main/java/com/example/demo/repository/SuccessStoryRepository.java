package com.example.demo.repository;

import com.example.demo.model.SuccessStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuccessStoryRepository extends JpaRepository<SuccessStory, Long> {
    List<SuccessStory> findByApprovedTrueOrderByCreatedAtDesc();
    List<SuccessStory> findByApprovedFalseOrderByCreatedAtDesc();
} 