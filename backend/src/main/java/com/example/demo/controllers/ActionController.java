package com.example.demo.controllers;

import com.example.demo.entities.Action;
import com.example.demo.entities.User;
import com.example.demo.services.ActionService;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actions")
public class ActionController {

    private final ActionService service;
    private final UserService userService;

    public ActionController(ActionService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public List<Action> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Action getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public Action create(@RequestBody Action action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            try {
                String email = getEmailFromAuth(auth);
                User user = userService.findByEmail(email);
                if (user.getOrganisation() != null) {
                    action.setOrganisation(user.getOrganisation());
                }
            } catch (Exception e) {
                // Ignore if not found
            }
        }
        return service.save(action);
    }

    @PostMapping("/{id}/participate")
    public ResponseEntity<?> participate(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Non authentifié");
        }
        try {
            String email = getEmailFromAuth(auth);
            User user = userService.findByEmail(email);
            Action action = service.findById(id);
            if (!action.getParticipants().contains(user)) {
                action.getParticipants().add(user);
                user.getParticipations().add(action);
                service.save(action);
                userService.save(user);
            }
            return ResponseEntity.ok(action);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Action update(@PathVariable Long id, @RequestBody Action action) {
        return service.update(id, action);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }

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
}