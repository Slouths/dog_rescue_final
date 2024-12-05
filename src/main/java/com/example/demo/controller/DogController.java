package com.example.demo.controller;

import com.example.demo.model.Dog;
import com.example.demo.service.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DogController {
    @Autowired
    private DogService dogService;

    @GetMapping("/dogs")
    public String showDogs(Model model) {
        List<Dog> dogs = dogService.getFeaturedDogs();
        model.addAttribute("dogs", dogs);
        return "dogs";
    }
} 