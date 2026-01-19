package org.example.repository;

import org.example.entity.Bout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoutRepository extends JpaRepository<Bout, String> {



    @Query(
            value = """
            select bout_id, details
            from bout_details
            where details_key = :key
        """,
            nativeQuery = true
    )
    List<Object[]> findBoutDetailsByKey(@Param("key") String key);

}
