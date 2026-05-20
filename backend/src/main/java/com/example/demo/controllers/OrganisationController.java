package com.example.demo.controllers;

import com.example.demo.entities.Organisation;
import com.example.demo.services.OrganisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.example.demo.services.UserService;
import com.example.demo.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    private final OrganisationService service;
    private final UserService userService;

    public OrganisationController(OrganisationService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public List<Organisation> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Organisation getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public Organisation create(@RequestBody Organisation organisation) {
        Organisation savedOrg = service.save(organisation);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            try {
                String email = getEmailFromAuth(auth);
                User user = userService.findByEmail(email);
                user.setOrganisation(savedOrg);
                userService.save(user);
            } catch (Exception e) {
                // Log or ignore if user details are not found in current context (e.g. mock/test)
            }
        }
        return savedOrg;
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

    @PutMapping("/{id}")
    public Organisation update(@PathVariable Long id, @RequestBody Organisation organisation) {
        return service.update(id, organisation);
    }

     @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        service.approve(id);
        return ResponseEntity.ok("Organisation approuvée !");
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}