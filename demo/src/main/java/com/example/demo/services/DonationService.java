package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Action;
import com.example.demo.entities.Donation;
import com.example.demo.repos.ActionRepository;
import com.example.demo.repos.DonationRepository;

@Service
public class DonationService {

    private final DonationRepository repo;
    private final ActionRepository actionRepo;

    public DonationService(DonationRepository repo, ActionRepository actionRepo) {
        this.repo = repo;
        this.actionRepo = actionRepo;
    }

    public List<Donation> getAll() {
        return repo.findAll();
    }

    public Donation findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation non trouvee : " + id));
    }

    public List<Donation> getByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    
    public Donation save(Donation donation) {
    Donation saved = repo.save(donation);

    if (saved.getAction() != null && saved.getAction().getId() != null) {

        Action action = actionRepo
                .findById(saved.getAction().getId().longValue())
                .orElse(null);

        if (action != null) {
            Double total = repo.findByActionId(action.getId().longValue())
                    .stream()
                    .mapToDouble(Donation::getAmount)
                    .sum();

            action.setCurrentAmount(total);
            actionRepo.save(action);
        }
    }

    return saved;
}

    public void delete(Long id) {
        repo.deleteById(id);
    }
}