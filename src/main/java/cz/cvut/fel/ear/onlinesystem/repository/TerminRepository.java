package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TerminRepository extends JpaRepository<Termin, Integer> {
    @Query("""
       select t from Termin t
       where t.startTime <= :now and t.endTime >= :now
    """)
    List<Termin> findActive(@Param("now") LocalDateTime now);
}
