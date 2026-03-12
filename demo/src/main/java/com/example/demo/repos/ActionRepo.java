package com.example.demo.repos;

import com.example.demo.entities.Action;   // import de l'entité
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepo extends JpaRepository<Action, Integer> {

}