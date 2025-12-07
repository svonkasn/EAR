package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.mapper.StudentAnswerMapper;
import cz.cvut.fel.ear.onlinesystem.dto.request.GradeOpenAnswerRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.StudentAnswerResponse;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.service.impl.ManualGradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/answers")
public class ManualGradingController {
  private final ManualGradingService manualGradingService;
  private final StudentAnswerRepository answerRepo;
  private final StudentAnswerMapper answerMapper;

  public ManualGradingController(ManualGradingService manualGradingService,
                                 StudentAnswerRepository answerRepo,
                                 StudentAnswerMapper answerMapper) {
    this.manualGradingService = manualGradingService;
    this.answerRepo = answerRepo;
    this.answerMapper = answerMapper;
  }

  @PostMapping("/{answerId}/grade")
  public StudentAnswerResponse grade(@PathVariable int answerId,
                                     @RequestBody GradeOpenAnswerRequest request) {
    manualGradingService.gradeOpenAnswer(
            answerId,
            request.points(),
            request.teacherId(),
            request.note()
    );

    StudentAnswer a = answerRepo.findById(answerId)
            .orElseThrow(() -> new NotFoundException("Answer not found"));

    return answerMapper.toDto(a);
  }

  @PostMapping("/attempts/{attemptId}/finalize")
  public ResponseEntity<Void> finalizeAttemptIfAllGraded(@PathVariable int attemptId) {
    manualGradingService.finalizeIfAllGraded(attemptId);
    return ResponseEntity.ok().build();
  }
}
