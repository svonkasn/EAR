package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Role;
import cz.cvut.fel.ear.onlinesystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    /** Find a user by their unique username. */
    Optional<User> findByUsername(String username);

    /** Find a user by their unique email. */
    Optional<User> findByEmail(String email);

    /** Quick uniqueness checks. */
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    /** All users having a specific role. */
    List<User> findByRole(Role role);

    /** All students in a given group. */
    List<User> findAllByGroupOfStudentId(Integer groupId);

    /** All teachers assigned to a course (inverse side of coursesTaught). */
    List<User> findAllByCoursesTaughtId(Integer courseId);
}
