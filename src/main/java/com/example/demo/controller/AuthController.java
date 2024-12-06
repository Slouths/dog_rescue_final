package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginAttemptService loginAttemptService;

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String ip = getClientIP(request);
        if (!loginAttemptService.isBlocked(ip)) {
            model.addAttribute("attemptsRemaining", 
                loginAttemptService.getRemainingAttempts(ip));
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            if (userService.existsByUsername(user.getUsername())) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }
            
            user.setEnabled(true);
            userService.registerNewUser(user);
            return "redirect:/login?registered";
            
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
} 