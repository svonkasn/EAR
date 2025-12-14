package cz.cvut.fel.ear.onlinesystem.service.impl;


import cz.cvut.fel.ear.onlinesystem.exception.ConflictException;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.model.User;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.service.StudentTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {StudentTestServiceImpl.class, AutoGraderService.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentTestServiceImplTest {
  @Autowired
  private StudentTestService studentTestService;
  @MockitoBean
  private StudentTestRepository stRepo;
  @MockitoBean
  private StudentAnswerRepository answerRepo;
  @MockitoBean
  private AutoGraderService autoGraderService;

  /**
   * Integration test for {@link StudentTestService#submitAttempt(int, int)}.
   *
   * Scenario:
   *  - A student test is found and currently IN_PROGRESS.
   *  - There are no open answers left to grade.
   *
   * Expected:
   *  - The attempt is submitted successfully.
   *  - Auto-grading and score recomputation are triggered.
   *  - Test status is updated to EVALUATED.
   */
  @Test
  void submitAttempt_IntergationTest(){
    User u = new User();
    u.setId(1);

    StudentTest studentTest = new StudentTest();
    studentTest.setId(1);
    studentTest.setUser(u);
    studentTest.setStatus(StatusTest.IN_PROGRESS);

    when(stRepo.findById(1)).thenReturn(Optional.of(studentTest));
    when(answerRepo.countOpenNotGraded(studentTest.getId())).thenReturn(0L);

    studentTestService.submitAttempt(1,1);

    verify(autoGraderService).gradeClosedQuestions(1);
    verify(autoGraderService).recomputeTotalScore(1);
    assertEquals(StatusTest.EVALUATED, studentTest.getStatus());
  }

  /**
   * Verifies that submitting a non-existent test attempt throws a NotFoundException.
   * Expected: no grading or recomputation occurs.
   */
  @Test
  void submitAttempt_NotFound_ThrowsException() {
    // Arrange
    when(stRepo.findById(1)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> {
      studentTestService.submitAttempt(1, 1);
    });

    verify(autoGraderService, never()).gradeClosedQuestions(anyInt());
    verify(autoGraderService, never()).recomputeTotalScore(anyInt());
  }

  /**
   * Verifies that submitting an already submitted test (not IN_PROGRESS)
   * throws a ConflictException and prevents further grading.
   */
  @Test
  void submitAttempt_NotInProgress_ThrowsConflictException() {
    StudentTest studentTest = new StudentTest();
    // Arrange
    studentTest.setStatus(StatusTest.SUBMITTED);
    when(stRepo.findById(1)).thenReturn(Optional.of(studentTest));

    // Act & Assert
    assertThrows(ConflictException.class, () -> {
      studentTestService.submitAttempt(1, 1);
    });

    verify(autoGraderService, never()).gradeClosedQuestions(anyInt());
    verify(autoGraderService, never()).recomputeTotalScore(anyInt());
  }

}
