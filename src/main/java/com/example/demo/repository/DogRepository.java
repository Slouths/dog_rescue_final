package com.example.demo.repository;

import com.example.demo.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findByFeaturedTrue();
} 