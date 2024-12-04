package com.example.demo.service;

import com.example.demo.model.User;

public interface UserService {
    User findByUsername(String username);
    User save(User user);
    boolean existsByUsername(String username);
    User registerNewUser(User user);
} 