package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    /**
     * Find course by its unique code.
     * @param code unique course code
     * @return course if present
     */
    Optional<Course> findByCode(String code);

    /**
     * Load a Course together with its groups in a single query to avoid N+1.
     * <p>
     * {@code @EntityGraph(attributePaths = "groups")} tells JPA to eagerly fetch the "groups"
     * association only for this method call (does not change default LAZY/EAGER on the entity).
     *
     * @param id course id
     * @return course with initialized groups if found
     */
    @EntityGraph(attributePaths = "groups")
    Optional<Course> findWithGroupsById(Integer id);
}
