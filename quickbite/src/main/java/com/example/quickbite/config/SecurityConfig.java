package com.example.quickbite.config;

import com.example.quickbite.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Allow ALL static HTML pages
                .requestMatchers(
                    "/", "/index.html",
                    "/auth/**", "/menu/**", "/cart/**",
                    "/order/**", "/admin/**",
                    "/css/**", "/js/**", "/images/**"
                ).permitAll()
                // Allow WebSocket
                .requestMatchers("/ws/**").permitAll()
                // Allow auth APIs
                .requestMatchers("/api/auth/**").permitAll()
                // Admin APIs need ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // All other API calls need authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}