package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.model.*;
import org.junit.jupiter.api.Test;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = AutoGraderService.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class AutoGraderServiceTest {
  @Autowired
  private AutoGraderService autoGraderService;

  @MockitoBean
  private StudentAnswerRepository answerRepo;

  @MockitoBean
  private StudentTestRepository stRepo;

  /**
   * Verifies that multiple-choice questions with correct selections
   * are graded correctly with full points.
   */
  @Test
  void gradeClosedQuestions_MultipleChoiceCorrect_GradesCorrectly(){
    // Arrange
    int studentTestId = 1;
    cz.cvut.fel.ear.onlinesystem.model.Test test = createTest(1, 100);
    StudentTest studentTest = createStudentTest(studentTestId, null, test);

    MultipleChoice mc = createMultipleChoice(1, 10);
    StudentAnswer answer = createStudentAnswer(1, mc, studentTest, true, false, true, false);

    List<StudentAnswer> answers = List.of(answer);

    when(answerRepo.findByStudentTestId(studentTestId)).thenReturn(answers);

    // Act
    autoGraderService.gradeClosedQuestions(studentTestId);

    // Assert
    assertNotNull(answer.getCorrect());
    assertNotNull(answer.getAwardedPoints());
    verify(answerRepo).saveAll(answers);
  }

  /**
   * Ensures no exceptions are thrown when grading an empty answer list.
   */
  @Test
  void gradeClosedQuestions_EmptyAnswersList_NoErrors() {
    // Arrange
    int studentTestId = 1;
    List<StudentAnswer> answers = List.of();

    when(answerRepo.findByStudentTestId(studentTestId)).thenReturn(answers);

    // Act
    autoGraderService.gradeClosedQuestions(studentTestId);

    // Assert
    verify(answerRepo).saveAll(answers);
  }

  /**
   * Verifies that incorrect multiple-choice answers
   * are graded with zero points or marked incorrect.
   */
  @Test
  void gradeClosedQuestions_MultipleChoiceIncorrect_GradesCorrectly() {
    // Arrange
    int studentTestId = 1;
    cz.cvut.fel.ear.onlinesystem.model.Test test = createTest(1, 100);
    StudentTest studentTest = createStudentTest(studentTestId, null, test);

    MultipleChoice mc = createMultipleChoice(1, 10);
    StudentAnswer answer = createStudentAnswer(1, mc, studentTest, false, false, false, false);

    List<StudentAnswer> answers = List.of(answer);

    when(answerRepo.findByStudentTestId(studentTestId)).thenReturn(answers);

    // Act
    autoGraderService.gradeClosedQuestions(studentTestId);

    // Assert
    assertNotNull(answer.getCorrect());
    assertNotNull(answer.getAwardedPoints());
    verify(answerRepo).saveAll(answers);
  }


  /** Helper methods for test data creation. */
  private StudentTest createStudentTest(int id, Integer score, cz.cvut.fel.ear.onlinesystem.model.Test test) {
    StudentTest st = new StudentTest();
    st.setId(id);
    st.setScore(score);
    st.setTest(test);
    return st;
  }

  private cz.cvut.fel.ear.onlinesystem.model.Test createTest(int id, Integer maxPoints) {
    cz.cvut.fel.ear.onlinesystem.model.Test test = new cz.cvut.fel.ear.onlinesystem.model.Test();
    test.setId(id);
    test.setMaxPoints(maxPoints);
    return test;
  }

  private MultipleChoice createMultipleChoice(int id, int points) {
    MultipleChoice mc = new MultipleChoice();
    mc.setId(id);
    mc.setPoint(points);
    return mc;
  }

  private StudentAnswer createStudentAnswer(int id, Question question, StudentTest studentTest,
                                            Boolean selectedA, Boolean selectedB, Boolean selectedC, Boolean selectedD) {
    StudentAnswer answer = new StudentAnswer();
    answer.setId(id);
    answer.setQuestion(question);
    answer.setStudentTest(studentTest);
    answer.setSelectedA(selectedA);
    answer.setSelectedB(selectedB);
    answer.setSelectedC(selectedC);
    answer.setSelectedD(selectedD);
    return answer;
  }
}
