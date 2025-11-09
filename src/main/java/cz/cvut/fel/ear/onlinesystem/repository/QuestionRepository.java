package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    /**
     * Load all questions that belong to a given test.
     * <p>
     * Useful when preparing attempts or exporting test metadata.
     *
     * @param testId owning test id
     * @return questions for the test (possibly empty)
     */
    List<Question> findByTestId(Integer testId);

    /**
     * Count how many questions a test contains.
     * @param testId test id
     * @return number of questions
     */
    long countByTestId(Integer testId);

    /**
     * Sum of declared points for all questions in the test.
     * <p>
     * {@code coalesce(..., 0)} ensures 0 is returned when a test has no questions.
     *
     * @param testId test id
     * @return sum of question points
     */
    @Query("select coalesce(sum(q.point),0) from Question q where q.test.id=:testId")
    Integer sumPoints(@Param("testId") Integer testId);
}
