package org.example.repository;

import org.example.entity.Bout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoutRepository extends JpaRepository<Bout, String> {}
