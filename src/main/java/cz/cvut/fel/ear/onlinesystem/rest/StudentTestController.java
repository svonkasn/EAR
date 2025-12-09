package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;
import cz.cvut.fel.ear.onlinesystem.service.StudentTestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/tests")
public class StudentTestController {
  private final StudentTestService studentTestService;

  public StudentTestController(StudentTestService studentTestService) {
    this.studentTestService = studentTestService;
  }

  @PreAuthorize("hasRole('teacher') or hasRole('admin')")
  @GetMapping("/{testId}/stats")
  public TestStats getTestStats(@PathVariable int testId) {
    return studentTestService.computeStatsForTest(testId);
  }

  @PreAuthorize("hasRole('teacher') or hasRole('admin')")
  @GetMapping("/{testId}/low")
  public List<LeaderboardEntry> getLowTests(@PathVariable int testId, @RequestParam(defaultValue = "10") int number) {
    return studentTestService.lowN(testId, number );
  }

  @PreAuthorize("hasRole('teacher') or hasRole('admin')")
  @GetMapping("/{testId}/top")
  public List<LeaderboardEntry> getTopTests(@PathVariable int testId, @RequestParam(defaultValue = "10") int number) {
    return studentTestService.topN(testId, number );
  }
}
