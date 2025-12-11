package cz.cvut.fel.ear.onlinesystem.rest;


import cz.cvut.fel.ear.onlinesystem.dto.mapper.StudentTestMapper;
import cz.cvut.fel.ear.onlinesystem.dto.request.AttemptSubmitRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.StudentTestResponse;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.*;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.TestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.UserRepository;
import cz.cvut.fel.ear.onlinesystem.service.AttemptService;
import cz.cvut.fel.ear.onlinesystem.service.security.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/attempts")
public class AttemptController {
  private final AttemptService attemptService;
  private final StudentTestRepository studentTestRepository;
  private final UserRepository userRepository;
  private final TestRepository testRepository;
  private final StudentTestMapper studentTestMapper;
  private final SecurityService securityService;

  public AttemptController(AttemptService attemptService,
                           StudentTestRepository studentTestRepository,
                           UserRepository userRepository,
                           TestRepository testRepository,
                           StudentTestMapper studentTestMapper,
                           SecurityService securityService) {
    this.attemptService = attemptService;
    this.studentTestRepository = studentTestRepository;
    this.userRepository = userRepository;
    this.testRepository = testRepository;
    this.studentTestMapper = studentTestMapper;
    this.securityService = securityService;
  }


  /* CREATE */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and #req.userId == @securityService.getCurrentUserId())")
  @PostMapping
  public StudentTestResponse createAttempt(@RequestBody StudentTestCreateRequest req) {
    User user = userRepository.findById(req.userId())
            .orElseThrow(() -> new NotFoundException("User not found"));
    Test test = testRepository.findById(req.testId())
            .orElseThrow(() -> new NotFoundException("Test not found"));

    StudentTest st = new StudentTest();
    st.setUser(user);
    st.setTest(test);
    st.setStatus(StatusTest.IN_PROGRESS);
    st.setScore(null);

    studentTestRepository.save(st);
    return studentTestMapper.toDto(st);
  }

  /* READ ONE */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @securityService.isOwnedAttempt(#id))")
  @GetMapping("/{id}")
  public StudentTestResponse getAttempt(@PathVariable int id) {
    StudentTest st = studentTestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Attempt not found"));
    return studentTestMapper.toDto(st);
  }

  /* READ LIST */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
  @GetMapping
  public List<StudentTestResponse> listAttempts(@RequestParam(required = false) Integer userId,
                                                @RequestParam(required = false) Integer testId) {
    boolean isStudent = securityService.isCurrentUserStudent();

    List<StudentTest> attempts;

    if (isStudent) {
      Integer currentUserId = securityService.getCurrentUserId();

      if (currentUserId == null) {
        throw new org.springframework.security.access.AccessDeniedException("Not authenticated");
      }

      if (testId != null) {
        attempts = studentTestRepository.findByUserAndTest(currentUserId, testId);
      }  else {
        attempts = studentTestRepository.findByUserId(currentUserId);
      }
    } else {
      if (userId != null && testId != null) {
        attempts = studentTestRepository.findByUserAndTest(userId, testId);
      } else if (userId != null) {
        attempts = studentTestRepository.findByUserId(userId);
      } else if (testId != null) {
        attempts = studentTestRepository.findByTestId(testId);
      } else {
        attempts = studentTestRepository.findAll();
      }
    }

    return attempts.stream()
            .map(studentTestMapper::toDto)
            .toList();
  }

  /* UPDATE */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @PutMapping("/{id}")
  public StudentTestResponse updateAttempt(@PathVariable int id,
                                           @RequestBody StudentTestUpdateRequest req) {
    StudentTest st = studentTestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Attempt not found"));

    if (req.status() != null) {
      st.setStatus(StatusTest.valueOf(req.status()));
    }
    studentTestRepository.save(st);
    return studentTestMapper.toDto(st);
  }

  /* DELETE */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAttempt(@PathVariable int id) {
    if (!studentTestRepository.existsById(id)) {
      throw new NotFoundException("Attempt not found");
    }
    studentTestRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  /* SUBMIT */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @securityService.isOwnedAttempt(#attemptId))")
  @PostMapping("/{attemptId}/submit")
  public ResponseEntity<Void> submit(@PathVariable int attemptId, @RequestBody AttemptSubmitRequest request) {
    attemptService.submitAttempt(attemptId, request.userId());
    return ResponseEntity.ok().build();
  }
}
