package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;
import cz.cvut.fel.ear.onlinesystem.service.StudentTestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/tests")
public class StudentTestController {
  private final StudentTestService studentTestService;

  public StudentTestController(StudentTestService studentTestService) {
    this.studentTestService = studentTestService;
  }

  @GetMapping("/{testId}/stats")
  public TestStats getTestStats(@PathVariable int testId) {
    return studentTestService.computeStatsForTest(testId);
  }

  @GetMapping("/{testId}/top")
  public List<LeaderboardEntry> getTopTests(@PathVariable int testId, @RequestParam(defaultValue = "10") int number) {
    return studentTestService.topN(testId, number );
  }
}
