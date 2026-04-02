package com.example.demo.controllers;

import com.example.demo.entities.Donation;
import com.example.demo.services.DonationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService service;

    public DonationController(DonationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Donation> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Donation getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public Donation create(@RequestBody Donation donation) { return service.save(donation); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}