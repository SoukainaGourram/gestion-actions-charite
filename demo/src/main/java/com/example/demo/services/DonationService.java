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
    private final EmailService emailService;

    public DonationService(DonationRepository repo, ActionRepository actionRepo, EmailService emailService) {
        this.repo = repo;
        this.actionRepo = actionRepo;
        this.emailService = emailService;
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

        if (saved.getAction() != null && saved.getAction().getId() != null && "COMPLETED".equals(saved.getStatus())) {
            updateActionTotal(saved.getAction().getId());
            sendReceiptEmail(saved);
        }

        return saved;
    }

    public Donation completeDonation(Long donationId) {
        Donation donation = findById(donationId);
        if (donation == null || "COMPLETED".equals(donation.getStatus())) {
            return donation;
        }

        donation.setStatus("COMPLETED");
        Donation saved = repo.save(donation);

        if (saved.getAction() != null && saved.getAction().getId() != null) {
            updateActionTotal(saved.getAction().getId());
        }

        sendReceiptEmail(saved);
        return saved;
    }

    private void updateActionTotal(Long actionId) {
        Action action = actionRepo.findById(actionId).orElse(null);
        if (action == null) {
            return;
        }

        Double total = repo.findByActionIdAndStatus(action.getId(), "COMPLETED")
                .stream()
                .mapToDouble(Donation::getAmount)
                .sum();
        action.setCurrentAmount(total);
        actionRepo.save(action);
    }

    private void sendReceiptEmail(Donation donation) {
        try {
            emailService.sendDonationReceipt(donation);
        } catch (Exception ex) {
            // Intentionally swallow mail delivery failures so donation processing stays functional.
        }
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}