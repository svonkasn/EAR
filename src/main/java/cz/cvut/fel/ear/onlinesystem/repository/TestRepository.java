package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Test;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Integer> {
    /** All tests scheduled in a specific term. */
    List<Test> findByTerminId(Integer terminId);

    /** All tests assigned to a specific student group. */
    List<Test> findByGroupId(Integer groupId);

    /**
     * Unique test scheduled for a given (termin, group) pair.
     * @return Optional test if such combination exists
     */
    @Query("select t from Test t where t.termin.id = :terminId and t.group.id = :groupId")
    Optional<Test> findByTerminAndGroup(@Param("terminId") Integer terminId,
                                        @Param("groupId") Integer groupId);

    /**
     * Load a test together with its questions in a single query to avoid N+1.
     */
    @EntityGraph(attributePaths = {"questions"}) Optional<Test> findWithQuestionsById(Integer id);

    /**
     * Fast existence check to prevent duplicates for (group, termin) scheduling.
     */
    boolean existsByGroupIdAndTerminId(Integer groupId, Integer terminId);
}
