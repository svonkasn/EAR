package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.model.User;
import cz.cvut.fel.ear.onlinesystem.repository.QuestionRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.TestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {TestStatisticsServiceImpl.class})
@TestPropertySource(locations = "classpath:application-test.properties")
class TestServiceImplTestStatistics {

  @Autowired
  private TestStatisticsServiceImpl studentTestService;

  @MockitoBean
  private StudentTestRepository studentTestRepo;

  @MockitoBean
  private TestRepository testRepo;

  @MockitoBean
  private QuestionRepository questionRepo;

  /**
   * Tests computation of test statistics when valid data is available.
   * Verifies average, min, max, median, p90, and histogram counts.
   */
  @Test
  void computeStatsForTest_ValidData_ReturnsCorrectStatistics() {
    // Arrange
    int testId = 1;
    cz.cvut.fel.ear.onlinesystem.model.Test test = createTest(testId, 100, "Math Test");
    List<Integer> sortedScores = Arrays.asList(78, 85, 88, 90, 92);

    setupBasicMocks(testId, test, 5L, 5L, 85.5, 60, 100, 100, 5L, sortedScores);

    List<StudentTestRepository.StatusCountView> statusData = List.of(
      createStatusCountView(StatusTest.EVALUATED, 5L)
    );
    setupStatusHistogramMock(testId, statusData);

    // Act
    TestStats stats = studentTestService.computeStatsForTest(testId);

    // Assert
    assertBasicStats(stats, testId, 5L, 5L, 100, 100, 85.5, 60, 88, 92, 100);

    assertEquals(1, stats.statusHistogram().size());
    assertStatusHistogram(stats, StatusTest.EVALUATED, 5L);
  }

  /**
   * Verifies that tests with multiple student statuses return
   * the correct histogram breakdown.
   */
  @Test
  void computeStatsForTest_MultipleStatuses_ReturnsCorrectHistogram() {
    // Arrange
    int testId = 1;
    cz.cvut.fel.ear.onlinesystem.model.Test test = createTest(testId, 100, "Test tests");
    List<Integer> sortedScores = Arrays.asList(50, 65, 70, 75, 80, 85, 95);

    setupBasicMocks(testId, test, 10L, 8L, 75.0, 50, 95, 100, 7L, sortedScores);

    List<StudentTestRepository.StatusCountView> statusData = List.of(
      createStatusCountView(StatusTest.EVALUATED, 7L),
      createStatusCountView(StatusTest.IN_PROGRESS, 2L),
      createStatusCountView(StatusTest.NOT_STARTED, 1L)
    );
    setupStatusHistogramMock(testId, statusData);

    // Act
    TestStats stats = studentTestService.computeStatsForTest(testId);

    // Assert
    assertBasicStats(stats, testId, 10L, 8L, 100, 100, 75.0, 50, 75, 95, 95);

    assertEquals(3, stats.statusHistogram().size());
    assertStatusHistogram(stats, StatusTest.EVALUATED, 7L);
    assertStatusHistogram(stats, StatusTest.IN_PROGRESS, 2L);
    assertStatusHistogram(stats, StatusTest.NOT_STARTED, 1L);
  }

  /**
   * Tests that topN() returns a correct leaderboard ordered by score descending.
   */
  @Test
  void topN_ValidData_ReturnsLeaderboard() {
    // Arrange
    int testId = 1;
    int n = 3;

    User user1 = createUser(1, "user1", "user1@mail.com");
    User user2 = createUser(2, "user2", "user2@mail.com");
    User user3 = createUser(3, "user3", "user3@mail.com");

    StudentTest test1 = createStudentTest(1, 95, user1);
    StudentTest test2 = createStudentTest(2, 90, user2);
    StudentTest test3 = createStudentTest(3, 85, user3);

    List<StudentTest> topTests = Arrays.asList(test1, test2, test3);
    Page<StudentTest> page = new PageImpl<>(topTests, PageRequest.of(0, n), 3);

    when(studentTestRepo.findByTestIdAndScoreIsNotNull(eq(testId), any())).thenReturn(page);

    // Act
    List<LeaderboardEntry> result = studentTestService.topN(testId, n);

    // Assert
    assertEquals(3, result.size());
    assertLeaderboardEntry(result.get(0), 1, 95, "user1", "user1@mail.com");
    assertLeaderboardEntry(result.get(1), 2, 90, "user2", "user2@mail.com");
    assertLeaderboardEntry(result.get(2), 3, 85, "user3", "user3@mail.com");
  }

  /**
   * Tests that lowN() returns a list of the lowest scores ordered ascending.
   */
  @Test
  void lowN_ValidData_ReturnsBottomScores() {
    // Arrange
    int testId = 1;
    int n = 2;

    User user1 = createUser(1, "user1", "user1@mail.com");
    User user2 = createUser(2, "user2", "user2@mail.com");

    StudentTest test1 = createStudentTest(1, 45, user1);
    StudentTest test2 = createStudentTest(2, 50, user2);

    List<StudentTest> lowTests = Arrays.asList(test1, test2);
    Page<StudentTest> page = new PageImpl<>(lowTests, PageRequest.of(0, n), 2);

    when(studentTestRepo.findByTestIdAndScoreIsNotNull(eq(testId), any())).thenReturn(page);

    // Act
    List<LeaderboardEntry> result = studentTestService.lowN(testId, n);

    // Assert
    assertEquals(2, result.size());
    assertLeaderboardEntry(result.get(0), 1, 45, "user1", "user1@mail.com");
    assertLeaderboardEntry(result.get(1), 2, 50, "user2", "user2@mail.com");
  }

  /**
   * Ensures that if there are no graded attempts,
   * score-related statistics (avg, min, etc.) are null.
   */
  @Test
  void computeStatsForTest_NoGradedAttempts_ReturnsNullForScoreStatistics() {
    // Arrange
    int testId = 1;
    cz.cvut.fel.ear.onlinesystem.model.Test test = createTest(testId, 100, "Empty Test");

    setupBasicMocks(testId, test, 3L, 2L, null, null, null, 100, 0L, List.of());
    setupStatusHistogramMock(testId, List.of());

    // Act
    TestStats stats = studentTestService.computeStatsForTest(testId);

    // Assert
    assertBasicStats(stats, testId, 3L, 2L, 100, 100, null, null, null, null, null);
    assertTrue(stats.statusHistogram().isEmpty());
  }


  /** Helper methods for creating entities and setting up mocks. */
  private cz.cvut.fel.ear.onlinesystem.model.Test createTest(int id, int maxPoints, String title) {
    cz.cvut.fel.ear.onlinesystem.model.Test test = new cz.cvut.fel.ear.onlinesystem.model.Test();
    test.setId(id);
    test.setMaxPoints(maxPoints);
    test.setTitle(title);
    return test;
  }

  private User createUser(int id, String username, String email) {
    User user = new User();
    user.setId(id);
    user.setUsername(username);
    user.setEmail(email);
    return user;
  }

  private StudentTest createStudentTest(int id, Integer score, User user, StatusTest status) {
    StudentTest test = new StudentTest();
    test.setId(id);
    test.setScore(score);
    test.setUser(user);
    test.setStatus(status);
    return test;
  }


  private StudentTest createStudentTest(int id, Integer score, User user) {
    return createStudentTest(id, score, user, StatusTest.EVALUATED);
  }


  private StudentTestRepository.StatusCountView createStatusCountView(StatusTest status, long count) {
    StudentTestRepository.StatusCountView view = mock(StudentTestRepository.StatusCountView.class);
    when(view.getStatus()).thenReturn(status);
    when(view.getCount()).thenReturn(count);
    return view;
  }


  private void setupBasicMocks(int testId, cz.cvut.fel.ear.onlinesystem.model.Test test,
                               long attempts, long distinctStudents, Double averageScore,
                               Integer minScore, Integer maxScore, Integer sumPoints,
                               long gradedAttempts, List<Integer> sortedScores) {
    when(testRepo.findById(testId)).thenReturn(Optional.of(test));
    when(studentTestRepo.attemptsCount(testId)).thenReturn(attempts);
    when(studentTestRepo.distinctStudents(testId)).thenReturn(distinctStudents);
    when(studentTestRepo.averageScore(testId)).thenReturn(averageScore);
    when(studentTestRepo.minScore(testId)).thenReturn(minScore);
    when(studentTestRepo.maxScore(testId)).thenReturn(maxScore);
    when(questionRepo.sumPoints(testId)).thenReturn(sumPoints);
    when(studentTestRepo.countByTestIdAndScoreIsNotNull(testId)).thenReturn(gradedAttempts);
    when(studentTestRepo.scoresSorted(testId)).thenReturn(sortedScores);
  }

  private void setupStatusHistogramMock(int testId, List<StudentTestRepository.StatusCountView> statusViews) {
    when(studentTestRepo.statusHistogram(testId)).thenReturn(statusViews);
  }


  private void assertBasicStats(TestStats stats, int testId, long attempts, long distinctStudents,
                                Integer maxPoints, Integer sumQuestionPoints, Double averageScore,
                                Integer minScore, Integer medianScore, Integer p90, Integer maxScore) {
    assertNotNull(stats);
    assertEquals(testId, stats.testId());
    assertEquals(attempts, stats.attempts());
    assertEquals(distinctStudents, stats.distinctStudents());
    assertEquals(maxPoints, stats.maxPoints());
    assertEquals(sumQuestionPoints, stats.sumQuestionPoints());
    assertEquals(averageScore, stats.averageScore());
    assertEquals(minScore, stats.minScore());
    assertEquals(medianScore, stats.medianScore());
    assertEquals(p90, stats.p90());
    assertEquals(maxScore, stats.maxScore());
  }

  private void assertStatusHistogram(TestStats stats, StatusTest expectedStatus, long expectedCount) {
    assertTrue(stats.statusHistogram().stream()
        .anyMatch(bucket -> bucket.status() == expectedStatus && bucket.count() == expectedCount),
      "Expected status " + expectedStatus + " with count " + expectedCount);
  }

  private void assertLeaderboardEntry(LeaderboardEntry entry, int expectedRank, int expectedScore,
                                      String expectedUsername, String expectedEmail) {
    assertEquals(expectedRank, entry.rank());
    assertEquals(expectedScore, entry.score());
    assertEquals(expectedUsername, entry.username());
    assertEquals(expectedEmail, entry.email());
  }
}
