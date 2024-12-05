package com.example.demo.controller;

import com.example.demo.model.Dog;
import com.example.demo.service.DogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final DogService dogService;

    public HomeController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Dog> featuredDogs = dogService.getFeaturedDogs();
        model.addAttribute("featuredDogs", featuredDogs);
        return "home";
    }

    @GetMapping("/adoption")
    public String adoptionPortal(Model model) {
        List<Dog> allDogs = dogService.findAll();
        model.addAttribute("dogs", allDogs);
        return "adoption";
    }

    @GetMapping("/donation")
    public String donation() {
        return "donation";
    }

    @GetMapping("/submitstory")
    public String submitStory() {
        return "submitstory";
    }
} 