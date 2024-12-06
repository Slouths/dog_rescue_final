package com.example.demo.controller;

import com.example.demo.model.Dog;
import com.example.demo.service.DogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String adoptionPortal(@RequestParam(required = false) List<String> breeds, Model model) {
        List<String> availableBreeds = dogService.getAllBreeds();
        model.addAttribute("availableBreeds", availableBreeds);
        
        if (breeds != null && !breeds.isEmpty()) {
            try {
                List<Dog> filteredDogs = dogService.findByBreeds(breeds);
                if (filteredDogs.isEmpty()) {
                    model.addAttribute("error", "No dogs found with the selected breeds");
                }
                model.addAttribute("dogs", filteredDogs);
                model.addAttribute("selectedBreeds", breeds);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("dogs", dogService.findAll());
            }
        } else {
            model.addAttribute("dogs", dogService.findAll());
        }
        
        return "adoption";
    }

    @GetMapping("/donation")
    public String donation() {
        return "donation";
    }
} 