package com.example.demo.service;

import com.example.demo.model.Dog;
import java.util.List;

public interface DogService {
    List<Dog> findAll();
    Dog findById(Long id);
    Dog save(Dog dog);
    void deleteById(Long id);
    List<Dog> getTopDogs();
} 