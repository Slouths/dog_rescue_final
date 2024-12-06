package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.security.CustomAuthenticationFailureHandler;
import com.example.demo.security.CustomAuthenticationSuccessHandler;
import com.example.demo.security.IpBlockFilter;
import com.example.demo.service.LoginAttemptService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final LoginAttemptService loginAttemptService;

    public SecurityConfig(CustomAuthenticationFailureHandler failureHandler,
                        CustomAuthenticationSuccessHandler successHandler,
                        LoginAttemptService loginAttemptService) {
        this.failureHandler = failureHandler;
        this.successHandler = successHandler;
        this.loginAttemptService = loginAttemptService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
            )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            .requestMatchers("/login", "/register").permitAll()
            .requestMatchers("/h2-console/**").permitAll()
            .requestMatchers("/api/submit-story").authenticated()
            .anyRequest().authenticated()
        )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .failureHandler(failureHandler)
                .successHandler(successHandler)
            )
            .addFilterBefore(new IpBlockFilter(loginAttemptService), 
                           UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 