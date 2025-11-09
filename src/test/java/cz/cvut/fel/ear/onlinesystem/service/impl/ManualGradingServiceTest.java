package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.exception.ValidationException;
import cz.cvut.fel.ear.onlinesystem.model.*;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ManualGradingService.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class ManualGradingServiceTest {

  @Autowired
  private ManualGradingService manualGradingService;
  @MockitoBean
  private StudentAnswerRepository answerRepo;
  @MockitoBean
  private StudentTestRepository stRepo;
  @MockitoBean
  private AutoGraderService autoGraderService;

  /**
   * Tests grading of an open question when full points are awarded.
   * Expected:
   *  - Answer is marked correct.
   *  - Points and teacher note are saved.
   *  - Total score is recomputed.
   */
  @Test
  void gradeOpenAnswer_ValidFullPoints_GradesCorrectly() {
    // Arrange
    int answerId = 1;
    int points = 10;
    int teacherId = 100;
    String note = "OKa!";

    OpenQuestion question = createOpenQuestion(1, 10);
    StudentTest studentTest = createStudentTest(1, StatusTest.SUBMITTED);
    StudentAnswer answer = createStudentAnswer(answerId, question, studentTest);

    when(answerRepo.findById(answerId)).thenReturn(Optional.of(answer));

    // Act
    manualGradingService.gradeOpenAnswer(answerId, points, teacherId, note);

    // Assert
    assertEquals(points, answer.getAwardedPoints());
    assertTrue(answer.getCorrect()); // Full points = correct
    assertEquals(note, answer.getTeacherNote());
    verify(answerRepo).save(answer);
    verify(autoGraderService).recomputeTotalScore(studentTest.getId());
  }

  /**
   * Verifies that grading a non-existent answer throws a NotFoundException.
   * No database writes or recomputation should occur.
   */
  @Test
  void gradeOpenAnswer_AnswerNotFound_ThrowsNotFoundException() {
    // Arrange
    int answerId = 999;
    when(answerRepo.findById(answerId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> {
      manualGradingService.gradeOpenAnswer(answerId, 5, 100, "Note");
    });

    verify(answerRepo, never()).save(any());
    verify(autoGraderService, never()).recomputeTotalScore(anyInt());
  }

  /**
   * Ensures that attempting to assign points above the question maximum
   * throws a ValidationException with an appropriate message.
   */
  @Test
  void gradeOpenAnswer_PointsAboveMax_ThrowsValidationException() {
    // Arrange
    int answerId = 1;
    OpenQuestion question = createOpenQuestion(1, 10);
    StudentAnswer answer = createStudentAnswer(answerId, question, createStudentTest(1, StatusTest.SUBMITTED));

    when(answerRepo.findById(answerId)).thenReturn(Optional.of(answer));

    // Act & Assert
    ValidationException exception = assertThrows(ValidationException.class, () -> {
      manualGradingService.gradeOpenAnswer(answerId, 11, 100, "Note");
    });

    assertEquals("Points out of range 0..10", exception.getMessage());
    verify(answerRepo, never()).save(any());
    verify(autoGraderService, never()).recomputeTotalScore(anyInt());
  }

  /**
   * Tests that when all open answers are graded,
   * the student test status transitions from SUBMITTED to EVALUATED.
   */
  @Test
  void finalizeIfAllGraded_NoPendingAnswers_SetsStatusToEvaluated() {
    // Arrange
    int studentTestId = 1;
    StudentTest studentTest = createStudentTest(studentTestId, StatusTest.SUBMITTED);

    when(answerRepo.countOpenNotGraded(studentTestId)).thenReturn(0L);
    when(stRepo.findById(studentTestId)).thenReturn(Optional.of(studentTest));

    // Act
    manualGradingService.finalizeIfAllGraded(studentTestId);

    // Assert
    assertEquals(StatusTest.EVALUATED, studentTest.getStatus());
    verify(stRepo).findById(studentTestId);
  }

  /** Helper method: creates an OpenQuestion with the given ID and max points. */
  private OpenQuestion createOpenQuestion(int id, int points) {
    OpenQuestion question = new OpenQuestion();
    question.setId(id);
    question.setPoint(points);
    return question;
  }

  /** Helper method: creates a StudentTest with specified status. */
  private StudentTest createStudentTest(int id, StatusTest status) {
    StudentTest st = new StudentTest();
    st.setId(id);
    st.setStatus(status);
    return st;
  }

  /** Helper method: creates a StudentAnswer linked to a given question and test. */
  private StudentAnswer createStudentAnswer(int id, Question question, StudentTest studentTest) {
    StudentAnswer answer = new StudentAnswer();
    answer.setId(id);
    answer.setQuestion(question);
    answer.setStudentTest(studentTest);
    return answer;
  }

}
