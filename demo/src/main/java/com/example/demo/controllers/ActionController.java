package com.example.demo.controllers;

import com.example.demo.entities.Action;
import com.example.demo.services.ActionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/actions")
public class ActionController {

    private final ActionService service;

    public ActionController(ActionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Action> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Action getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public Action create(@RequestBody Action action) { return service.save(action); }

    @PutMapping("/{id}")
    public Action update(@PathVariable Long id, @RequestBody Action action) {
        return service.update(id, action);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}