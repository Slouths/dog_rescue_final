package com.example.demo.repository;

import com.example.demo.model.SuccessStory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuccessStoryRepository extends JpaRepository<SuccessStory, Long> {
} 