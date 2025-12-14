package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;
import cz.cvut.fel.ear.onlinesystem.service.TestStatisticsExcelService;
import cz.cvut.fel.ear.onlinesystem.service.TestStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/tests")
public class TestStatisticsController {
  private final TestStatisticsService testStatisticsService;
  private final TestStatisticsExcelService excelService;

  public TestStatisticsController(TestStatisticsService testStatisticsService,
                                  TestStatisticsExcelService excelService) {
    this.testStatisticsService = testStatisticsService;
    this.excelService = excelService;
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @GetMapping("/{testId}/stats")
  public TestStats getTestStats(@PathVariable int testId) {
    return testStatisticsService.computeStatsForTest(testId);
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @GetMapping("/{testId}/low")
  public List<LeaderboardEntry> getLowTests(@PathVariable int testId, @RequestParam(defaultValue = "10") int number) {
    return testStatisticsService.lowN(testId, number );
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @GetMapping("/{testId}/top")
  public List<LeaderboardEntry> getTopTests(@PathVariable int testId, @RequestParam(defaultValue = "10") int number) {
    return testStatisticsService.topN(testId, number );
  }

  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @GetMapping(value = "/{testId}/stats.xlsx",
          produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
  public ResponseEntity<byte[]> exportStats(@PathVariable int testId,
                                            @RequestParam(defaultValue = "10") int number) {
    byte[] bytes = excelService.exportStats(testId, number);
    return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=test-" + testId + "-stats.xlsx")
            .body(bytes);
  }
}
