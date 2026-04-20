package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.repos.UserRepository;
import com.example.demo.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ INSCRIPTION
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email déjà utilisé !");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepo.save(user);

        return ResponseEntity.ok("Inscription réussie !");
    }

    // ✅ CONNEXION
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {

        String email = credentials.get("email");
        String password = credentials.get("password");

        // 🔍 Check user
        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Email introuvable !");
        }

        User user = userOpt.get();

        // 🔐 Check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("Mot de passe incorrect !");
        }

        // 🎟 Generate JWT
        String token = jwtUtil.generateToken(email);

        // 📦 Response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("email", email);
        response.put("role", user.getRole());

        return ResponseEntity.ok(response);
    }
}