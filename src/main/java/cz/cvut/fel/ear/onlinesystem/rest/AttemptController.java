package cz.cvut.fel.ear.onlinesystem.rest;


import cz.cvut.fel.ear.onlinesystem.dto.request.AttemptSubmitRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.StudentTestResponse;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.service.AttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/attempts")
public class AttemptController {
  private final AttemptService attemptService;
  private final StudentTestRepository studentTestRepository;

  public AttemptController(AttemptService attemptService, StudentTestRepository studentTestRepository) {
    this.attemptService = attemptService;
    this.studentTestRepository = studentTestRepository;
  }
  @PostMapping("attemptId/submit")
  public ResponseEntity<Void> submit(@PathVariable int attemptId, @RequestBody AttemptSubmitRequest request) {
    // TODO: nejsem jista, ze je to spravne
    attemptService.submitAttempt(attemptId, request.userId());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public StudentTestResponse getAttempt(@PathVariable int id) {
    StudentTest st = studentTestRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Attempt not found"));

    return new StudentTestResponse(
      st.getId(),
      st.getScore(),
      st.getStatus().name(),
      st.getUser().getId(),
      st.getTest().getId()
    );
  }

}
