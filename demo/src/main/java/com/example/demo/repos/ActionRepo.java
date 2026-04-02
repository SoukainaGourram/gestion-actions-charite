package com.example.demo.repos;

import com.example.demo.entities.Action;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActionRepository extends JpaRepository<Action, Long> {
}