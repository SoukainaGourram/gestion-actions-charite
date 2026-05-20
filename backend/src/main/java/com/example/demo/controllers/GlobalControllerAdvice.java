package com.example.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.demo.security.JwtUtil;
import com.example.demo.services.UserService;
import com.example.demo.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @ModelAttribute("jwtToken")
    public String addJwtTokenToModel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            String role = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(r -> r.startsWith("ROLE_"))
                .map(r -> r.substring(5))
                .findFirst()
                .orElse("USER");
            return jwtUtil.generateToken(auth.getName(), role);
        }
        return null;
    }

    @ModelAttribute("currentUserId")
    public Long addCurrentUserIdToModel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            try {
                User user = userService.findByEmail(auth.getName());
                return user.getId();
            } catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}
