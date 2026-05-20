package com.example.demo.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.User;
import com.example.demo.services.UserService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public User getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuth(auth);
        return service.findByEmail(email);
    }

    @PutMapping("/profile")
    public User updateProfile(@RequestBody User updateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = getEmailFromAuth(auth);
        return service.updateProfile(email, updateRequest.getName(), updateRequest.getPhone());
    }

    private String getEmailFromAuth(Authentication auth) {
        if (auth == null) {
            throw new RuntimeException("Non authentifié");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            return ((OAuth2User) principal).getAttribute("email");
        }
        return principal.toString();
    }

    @GetMapping
    public List<User> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public User create(@RequestBody User user) { return service.save(user); }


    @PutMapping("/{id}/role")
    public User updateRole(@PathVariable Long id, @RequestBody String role) {
        String sanitizedRole = role.replace("\"", "").trim();
        User user = service.findById(id);
        user.setRole(sanitizedRole);
        return service.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}