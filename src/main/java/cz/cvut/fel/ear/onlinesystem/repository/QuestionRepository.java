package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByTestId(Integer testId);
    long countByTestId(Integer testId); // number of questions in the test
    @Query("select coalesce(sum(q.point),0) from Question q where q.test.id=:testId")
    Integer sumPoints(@Param("testId") Integer testId);
}
