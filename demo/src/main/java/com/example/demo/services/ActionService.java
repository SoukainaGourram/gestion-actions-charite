package com.example.demo.services;

import com.example.demo.entities.Action;
import com.example.demo.repos.ActionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ActionService {

    private final ActionRepository repo;

    public ActionService(ActionRepository repo) {
        this.repo = repo;
    }

    public List<Action> getAll() {
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
        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setLocation(updated.getLocation());
        existing.setGoalAmount(updated.getGoalAmount());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}