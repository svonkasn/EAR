package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {
    List<StudentAnswer> findByStudentTestId(Integer studentTestId);

    Optional<StudentAnswer> findByStudentTestIdAndQuestionId(Integer studentTestId, Integer questionId);

    @Query("""
       select count(a) from StudentAnswer a
       where a.studentTest.id = :stId
         and type(a.question) = OpenQuestion
         and a.awardedPoints is null
    """)
    long countOpenNotGraded(@Param("stId") Integer studentTestId);
}
