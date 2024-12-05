package com.example.demo.security;

import com.example.demo.service.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final LoginAttemptService loginAttemptService;

    public CustomAuthenticationFailureHandler(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        String ip = getClientIP(request);
        
        if (loginAttemptService.isBlocked(ip)) {
            response.sendRedirect("/login?error=blocked");
            return;
        }
        
        loginAttemptService.loginFailed(ip);
        int remainingAttempts = loginAttemptService.getRemainingAttempts(ip);
        
        if (remainingAttempts <= 0) {
            response.sendRedirect("/login?error=blocked");
        } else {
            request.getSession().setAttribute("attemptsRemaining", remainingAttempts);
            response.sendRedirect("/login?error=invalid");
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