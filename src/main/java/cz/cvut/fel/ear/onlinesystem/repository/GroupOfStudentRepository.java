package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.GroupOfStudent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupOfStudentRepository extends JpaRepository<GroupOfStudent, Integer> {
    List<GroupOfStudent> findByCourseId(Integer courseId);
}
