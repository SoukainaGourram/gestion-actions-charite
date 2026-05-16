package com.example.demo.controllers;

import com.example.demo.entities.Organisation;
import com.example.demo.services.OrganisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organisations")
public class OrganisationController {

    private final OrganisationService service;

    public OrganisationController(OrganisationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Organisation> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Organisation getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public Organisation create(@RequestBody Organisation organisation) { return service.save(organisation); }

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