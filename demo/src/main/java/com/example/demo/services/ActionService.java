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

    public Action save(Action action) {
        return repo.save(action);
    }
}