package com.example.demo.services;

import com.example.demo.entities.Action;
import com.example.demo.repos.ActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActionService {

    private final ActionRepository repo;

    public ActionService(ActionRepository repo) {
        this.repo = repo;
    }

    public List<Action> getAll() {
        return repo.findAll();
    }

    public List<Action> getAllOrByCategory(String category) {
        if (category != null && !category.isBlank()) {
            return repo.findByCategory(category); // ⚠️ nécessite repo corrigé
        }
        return repo.findAll();
    }

    public Action findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Action non trouvée : " + id));
    }

    public Action save(Action action) {
        return repo.save(action);
    }

    public Action update(Long id, Action updated) {
        Action existing = findById(id);

        if (updated.getTitle() != null)
            existing.setTitle(updated.getTitle());

        if (updated.getDescription() != null)
            existing.setDescription(updated.getDescription());

        if (updated.getLocation() != null)
            existing.setLocation(updated.getLocation());

        if (updated.getGoalAmount() != null)
            existing.setGoalAmount(updated.getGoalAmount());

        if (updated.getCategory() != null)
            existing.setCategory(updated.getCategory());

        if (updated.getStartDate() != null)
            existing.setStartDate(updated.getStartDate());

        if (updated.getEndDate() != null)
            existing.setEndDate(updated.getEndDate());

        if (updated.getImageUrl() != null)
            existing.setImageUrl(updated.getImageUrl());

        if (updated.getStatus() != null)
            existing.setStatus(updated.getStatus());

        return repo.save(existing);
    }

    public Map<String, Object> getProgress(Long id) {
        Action action = findById(id);

        return Map.of(
                "title", action.getTitle(),
                "goalAmount", action.getGoalAmount(),
                "currentAmount", action.getCurrentAmount(),
                "percentage", action.getProgressPercentage(),
                "status", action.getStatus()
        );
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Action introuvable pour suppression : " + id);
        }
        repo.deleteById(id);
    }
}