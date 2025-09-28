package org.example.repository;

import org.example.entity.Prime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrimeRepository extends JpaRepository<Prime, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM prime", nativeQuery = true)
    void truncateTable();

}
