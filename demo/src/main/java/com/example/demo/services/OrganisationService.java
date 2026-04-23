package com.example.demo.services;

import com.example.demo.entities.Organisation;
import com.example.demo.repos.OrganisationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrganisationService {

    private final OrganisationRepository repo;

    public OrganisationService(OrganisationRepository repo) {
        this.repo = repo;
    }

    public List<Organisation> getAll() {
        return repo.findAll();
    }

    public Organisation findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Organisation non trouvée : " + id));
    }

    public Organisation save(Organisation organisation) {
        return repo.save(organisation);
    }
    
    public Organisation update(Long id, Organisation updated) {
        Organisation existing = findById(id);
        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setFiscalNumber(updated.getFiscalNumber());
        existing.setContact(updated.getContact());
        existing.setLogoUrl(updated.getLogoUrl());
        existing.setDescription(updated.getDescription());
        return repo.save(existing);
    }
 
    //  (SUPER_ADMIN)
    public void approve(Long id) {
        Organisation org = findById(id);
        org.setApproved(true);
        repo.save(org);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}