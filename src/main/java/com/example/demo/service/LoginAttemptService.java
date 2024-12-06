package com.example.demo.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 1;
    
    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_MINUTES, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key) {
        int attempts;
        try {
            attempts = attemptsCache.get(key) + 1;
        } catch (ExecutionException e) {
            attempts = 1;
        }
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPTS;
        } catch (ExecutionException e) {
            return false;
        }
    }

    public int getRemainingAttempts(String key) {
        try {
            int attempts = attemptsCache.get(key);
            return Math.max(0, MAX_ATTEMPTS - attempts);
        } catch (ExecutionException e) {
            return MAX_ATTEMPTS;
        }
    }
} 