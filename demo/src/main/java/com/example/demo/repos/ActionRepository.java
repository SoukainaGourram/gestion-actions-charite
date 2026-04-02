package com.example.demo.repos;

import com.example.demo.entities.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByOrganisationId(Long organisationId);
}