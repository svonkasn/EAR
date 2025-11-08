package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.model.*;
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

  @org.junit.jupiter.api.Test
  void gradeClosedQuestions_MultipleChoiceCorrect_GradesCorrectly(){
    // Arrange
    int studentTestId = 1;
    Test test = createTest(1, 100);
    StudentTest studentTest = createStudentTest(studentTestId, null, test);

    MultipleChoice mc = createMultipleChoice(1, 10);
    StudentAnswer answer = createStudentAnswer(1, mc, studentTest, true, false, true, false, null, null);

    List<StudentAnswer> answers = List.of(answer);

    when(answerRepo.findByStudentTestId(studentTestId)).thenReturn(answers);

    // Act
    autoGraderService.gradeClosedQuestions(studentTestId);

    // Assert
    assertNotNull(answer.getCorrect());
    assertNotNull(answer.getAwardedPoints());
    verify(answerRepo).saveAll(answers);
  }
  @org.junit.jupiter.api.Test
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

  @org.junit.jupiter.api.Test
  void gradeClosedQuestions_MultipleChoiceIncorrect_GradesCorrectly() {
    // Arrange
    int studentTestId = 1;
    Test test = createTest(1, 100);
    StudentTest studentTest = createStudentTest(studentTestId, null, test);

    MultipleChoice mc = createMultipleChoice(1, 10);
    StudentAnswer answer = createStudentAnswer(1, mc, studentTest, false, false, false, false, null, null);

    List<StudentAnswer> answers = List.of(answer);

    when(answerRepo.findByStudentTestId(studentTestId)).thenReturn(answers);

    // Act
    autoGraderService.gradeClosedQuestions(studentTestId);

    // Assert
    assertNotNull(answer.getCorrect());
    assertNotNull(answer.getAwardedPoints());
    verify(answerRepo).saveAll(answers);
  }

  private StudentTest createStudentTest(int id, Integer score, Test test) {
    StudentTest st = new StudentTest();
    st.setId(id);
    st.setScore(score);
    st.setTest(test);
    return st;
  }

  private Test createTest(int id, Integer maxPoints) {
    Test test = new Test();
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
                                            Boolean selectedA, Boolean selectedB, Boolean selectedC, Boolean selectedD,
                                            Integer awardedPoints, Boolean correct) {
    StudentAnswer answer = new StudentAnswer();
    answer.setId(id);
    answer.setQuestion(question);
    answer.setStudentTest(studentTest);
    answer.setSelectedA(selectedA);
    answer.setSelectedB(selectedB);
    answer.setSelectedC(selectedC);
    answer.setSelectedD(selectedD);
    answer.setAwardedPoints(awardedPoints);
    answer.setCorrect(correct);
    return answer;
  }
}
