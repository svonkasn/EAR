package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {
    /**
     * Fetch all answers for a given attempt.
     * @param studentTestId attempt (StudentTest) id
     * @return answers (possibly empty)
     */
    List<StudentAnswer> findByStudentTestId(Integer studentTestId);

    /**
     * Fetch a single answer for a given attempt-question pair.
     * @param studentTestId attempt id
     * @param questionId question id
     * @return the answer if present
     */
    Optional<StudentAnswer> findByStudentTestIdAndQuestionId(Integer studentTestId, Integer questionId);

    /**
     * Count open (essay) answers that still need manual grading.
     * <p>
     * Uses JPA TYPE operator to filter by question subtype.
     * @param studentTestId attempt id
     * @return how many open answers do not have awardedPoints yet
     */
    @Query("""
       select count(a) from StudentAnswer a
       where a.studentTest.id = :stId
         and type(a.question) = OpenQuestion
         and a.awardedPoints is null
    """)
    long countOpenNotGraded(@Param("stId") Integer studentTestId);
}
