package com.example.demo.service;

import com.example.demo.model.Dog;
import com.example.demo.repository.DogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DogServiceImpl implements DogService {
    private final DogRepository dogRepository;

    public DogServiceImpl(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @Override
    public List<Dog> findAll() {
        return dogRepository.findAll();
    }

    @Override
    public Dog findById(Long id) {
        return dogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dog not found with id: " + id));
    }

    @Override
    public Dog save(Dog dog) {
        return dogRepository.save(dog);
    }

    @Override
    public void deleteById(Long id) {
        dogRepository.deleteById(id);
    }

    @Override
    public List<Dog> getTopDogs() {
        return dogRepository.findAll();
    }
} 