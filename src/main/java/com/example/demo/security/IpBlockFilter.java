package com.example.demo.security;

import com.example.demo.service.LoginAttemptService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class IpBlockFilter extends OncePerRequestFilter {
    private final LoginAttemptService loginAttemptService;

    public IpBlockFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response,
                                  FilterChain filterChain) 
            throws ServletException, IOException {
        
        String ip = getClientIP(request);
        
        // Only check login attempts for the login endpoint
        if (request.getRequestURI().equals("/login") && 
            request.getMethod().equals("POST") &&
            loginAttemptService.isBlocked(ip)) {
            response.sendRedirect("/login?error=blocked");
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
} 