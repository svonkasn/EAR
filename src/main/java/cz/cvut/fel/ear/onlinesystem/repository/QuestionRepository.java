package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    List<Question> findByTestId(Integer testId);
}
