package cz.cvut.fel.ear.onlinesystem.rest;


import cz.cvut.fel.ear.onlinesystem.dto.mapper.StudentTestMapper;
import cz.cvut.fel.ear.onlinesystem.dto.request.AttemptSubmitRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.StudentTestResponse;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.TestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.UserRepository;
import cz.cvut.fel.ear.onlinesystem.service.AttemptService;
import org.springframework.http.ResponseEntity;
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

  public AttemptController(AttemptService attemptService,
                           StudentTestRepository studentTestRepository,
                           UserRepository userRepository,
                           TestRepository testRepository,
                           StudentTestMapper studentTestMapper) {
    this.attemptService = attemptService;
    this.studentTestRepository = studentTestRepository;
    this.userRepository = userRepository;
    this.testRepository = testRepository;
    this.studentTestMapper = studentTestMapper;
  }


  /* CREATE */
  @PostMapping
  public StudentTestResponse createAttempt(@RequestBody StudentTestCreateRequest req) {
    var user = userRepository.findById(req.userId())
            .orElseThrow(() -> new NotFoundException("User not found"));
    var test = testRepository.findById(req.testId())
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
  @GetMapping("/{id}")
  public StudentTestResponse getAttempt(@PathVariable int id) {
    StudentTest st = studentTestRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Attempt not found"));
    return studentTestMapper.toDto(st);
  }

  /* READ LIST */
  @GetMapping
  public List<StudentTestResponse> listAttempts(@RequestParam(required = false) Integer userId,
                                                @RequestParam(required = false) Integer testId) {
    List<StudentTest> attempts;

    if (userId != null && testId != null) {
      attempts = studentTestRepository.findByUserIdAndTestId(userId, testId);
    } else if (userId != null) {
      attempts = studentTestRepository.findByUserId(userId);
    } else if (testId != null) {
      attempts = studentTestRepository.findByTestId(testId);
    } else {
      attempts = studentTestRepository.findAll();
    }

    return attempts.stream()
            .map(studentTestMapper::toDto)
            .toList();
  }

  /* UPDATE */
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
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAttempt(@PathVariable int id) {
    if (!studentTestRepository.existsById(id)) {
      throw new NotFoundException("Attempt not found");
    }
    studentTestRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  /* SUBMIT */
  @PostMapping("/{attemptId}/submit")
  public ResponseEntity<Void> submit(@PathVariable int attemptId, @RequestBody AttemptSubmitRequest request) {
    attemptService.submitAttempt(attemptId, request.userId());
    return ResponseEntity.ok().build();
  }



}
