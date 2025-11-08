package cz.cvut.fel.ear.onlinesystem.service.impl;


import cz.cvut.fel.ear.onlinesystem.exception.ConflictException;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.model.User;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.service.AttemptService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {AttemptServiceImpl.class, AutoGraderService.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class AttemptServiceImplTest {
  @Autowired
  private AttemptService attemptService;
  @MockitoBean
  private StudentTestRepository stRepo;
  @MockitoBean
  private StudentAnswerRepository answerRepo;
  @MockitoBean
  private AutoGraderService autoGraderService;

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

    attemptService.submitAttempt(1,1);

    verify(autoGraderService).gradeClosedQuestions(1);
    verify(autoGraderService).recomputeTotalScore(1);
    assertEquals(StatusTest.EVALUATED, studentTest.getStatus());
  }
  @Test
  void submitAttempt_NotFound_ThrowsException() {
    // Arrange
    when(stRepo.findById(1)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> {
      attemptService.submitAttempt(1, 1);
    });

    verify(autoGraderService, never()).gradeClosedQuestions(anyInt());
    verify(autoGraderService, never()).recomputeTotalScore(anyInt());
  }
  @Test
  void submitAttempt_NotInProgress_ThrowsConflictException() {
    StudentTest studentTest = new StudentTest();
    // Arrange
    studentTest.setStatus(StatusTest.SUBMITTED);
    when(stRepo.findById(1)).thenReturn(Optional.of(studentTest));

    // Act & Assert
    assertThrows(ConflictException.class, () -> {
      attemptService.submitAttempt(1, 1);
    });

    verify(autoGraderService, never()).gradeClosedQuestions(anyInt());
    verify(autoGraderService, never()).recomputeTotalScore(anyInt());
  }

}
