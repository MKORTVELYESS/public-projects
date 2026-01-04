package org.example.repository;

import org.example.entity.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FighterRepository extends JpaRepository<Fighter, String> {}
