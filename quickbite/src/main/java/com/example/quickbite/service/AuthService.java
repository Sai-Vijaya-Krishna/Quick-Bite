package com.example.quickbite.service;

import com.example.quickbite.dto.LoginRequest;
import com.example.quickbite.dto.RegisterRequest;
import com.example.quickbite.entity.User;
import com.example.quickbite.repository.UserRepository;
import com.example.quickbite.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmailService emailService;

    public Map<String, String> register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setAddress(req.getAddress());
        user.setRole("USER");
        userRepository.save(user);

        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        res.put("role", user.getRole());
        res.put("name", user.getName());
        return res;
    }

    public Map<String, String> login(LoginRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid password");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Map<String, String> res = new HashMap<>();
        res.put("token", token);
        res.put("role", user.getRole());
        res.put("name", user.getName());
        return res;
    }
}
