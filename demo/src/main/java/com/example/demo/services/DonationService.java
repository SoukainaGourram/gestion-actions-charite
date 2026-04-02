package com.example.demo.services;

import com.example.demo.entities.Donation;
import com.example.demo.repos.DonationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DonationService {

    private final DonationRepository repo;

    public DonationService(DonationRepository repo) {
        this.repo = repo;
    }

    public List<Donation> getAll() {
        return repo.findAll();
    }

    public Donation findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation non trouvée : " + id));
    }

    public Donation save(Donation donation) {
        return repo.save(donation);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}