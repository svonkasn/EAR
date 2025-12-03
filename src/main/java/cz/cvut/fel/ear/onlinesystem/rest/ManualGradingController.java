package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.request.GradeOpenAnswerRequest;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import cz.cvut.fel.ear.onlinesystem.service.impl.ManualGradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/answers")
public class ManualGradingController {
  private final ManualGradingService manualGradingService;
  public ManualGradingController(ManualGradingService manualGradingService) {
    this.manualGradingService = manualGradingService;
  }

  @PostMapping("answerId/grade")
  public ResponseEntity<Void> grade(@PathVariable int answerId, @RequestBody GradeOpenAnswerRequest request) {
    manualGradingService.gradeOpenAnswer(answerId, request.points(), request.teacherId(), request.note());
    return ResponseEntity.ok().build();
  }
}
