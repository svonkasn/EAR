package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.GroupOfStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupOfStudentRepository extends JpaRepository<GroupOfStudent, Integer> {
    /**
     * List all groups that belong to a given course.
     * <p>
     * Spring Data derives the query from the method name (filters by foreign key course.id).
     *
     * @param courseId owning course id
     * @return groups under the course (possibly empty)
     */
    List<GroupOfStudent> findByCourseId(Integer courseId);
}
