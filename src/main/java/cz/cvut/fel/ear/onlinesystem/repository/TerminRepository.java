package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TerminRepository extends JpaRepository<Termin, Integer> {
    /**
     * Find terms that are active at a given moment.
     * <p>
     * A term is considered active if startTime <= now <= endTime.
     *
     * @param now reference time
     * @return currently active terms
     */
    @Query("""
       select t from Termin t
       where t.startTime <= :now and t.endTime >= :now
    """)
    List<Termin> findActive(@Param("now") LocalDateTime now);
}
