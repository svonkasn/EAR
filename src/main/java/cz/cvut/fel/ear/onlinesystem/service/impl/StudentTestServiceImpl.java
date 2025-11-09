package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.StatusBucket;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.model.Test;
import cz.cvut.fel.ear.onlinesystem.model.User;
import cz.cvut.fel.ear.onlinesystem.repository.QuestionRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.TestRepository;
import cz.cvut.fel.ear.onlinesystem.service.StudentTestService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudentTestServiceImpl implements StudentTestService {

    private final StudentTestRepository studentTestRepo;
    private final TestRepository testRepo;
    private final QuestionRepository questionRepo;

    public StudentTestServiceImpl(StudentTestRepository studentTestRepo, TestRepository testRepo, QuestionRepository questionRepo) {
        this.studentTestRepo = studentTestRepo;
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
    }

    @Override
    public TestStats computeStatsForTest(int testId) {
        // Load test metadata (maxPoints etc.)
        Test test = testRepo.findById(testId).orElseThrow(()-> new NotFoundException("Test with id " + testId + " not found"));

        // Aggregates
        long attempts = studentTestRepo.attemptsCount(testId);
        long distinct = studentTestRepo.distinctStudents(testId);
        Double avg = studentTestRepo.averageScore(testId);
        Integer min = studentTestRepo.minScore(testId);
        Integer max = studentTestRepo.maxScore(testId);

        // Status histogram (NOT_STARTED / IN_PROGRESS / SUBMITTED / EVALUATED)
        List<StatusBucket> buckets = studentTestRepo.statusHistogram(testId).stream()
                .map(v -> new StatusBucket(v.getStatus(), v.getCount())).toList();

        // Sum of question points
        Integer sumPoints = questionRepo.sumPoints(testId);

        // Median / P90 computed in-memory using a pre-sorted score list
        long graded = studentTestRepo.countByTestIdAndScoreIsNotNull(testId);
        List<Integer> sorted = graded == 0 ? List.of() : studentTestRepo.scoresSorted(testId);
        Integer median = median(sorted);
        Integer p90 = percentile(sorted);

        // Cap metrics by test.getMaxPoints() just in case (defensive)
        return new TestStats(
                testId,
                attempts,
                distinct,
                test.getMaxPoints(),
                sumPoints,
                avg,
                min,
                median == null ? null : Math.min(median, test.getMaxPoints()),
                p90 == null ? null : Math.min(p90, test.getMaxPoints()),
                max,
                buckets);
    }

    @Override
    public List<LeaderboardEntry> topN(int testId, int n) {
        return toLeaderboard(studentTestRepo.findByTestIdAndScoreIsNotNull(testId, StudentTestRepository.topN(n)));
    }

    @Override
    public List<LeaderboardEntry> lowN(int testId, int n) {
        return toLeaderboard(studentTestRepo.findByTestIdAndScoreIsNotNull(testId, StudentTestRepository.lowN(n)));
    }

    /** Simple median on an ascending sorted list of Integer scores. */
    private static Integer median(List<Integer> sorted) {
        if (sorted == null || sorted.isEmpty()) return null;
        int n = sorted.size();
        if ((n & 1) == 1) return sorted.get(n / 2);
        return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2;
    }

    /** Nearest-rank percentile on an ascending sorted list of Integer scores. */
    private static Integer percentile(List<Integer> sorted) {
        if (sorted == null || sorted.isEmpty()) return null;
        int n = sorted.size();
        int rank = (int) Math.ceil((90 / 100.0) * n);
        rank = Math.max(1, Math.min(rank, n));
        return sorted.get(rank - 1);
    }

    /**
     * Map a paged list of attempts to leaderboard entries.
     * Rank continues across pages: rank = page * size + index + 1.
     */
    private static List<LeaderboardEntry> toLeaderboard(Page<StudentTest> page) {
        List<StudentTest> items = page.getContent();
        List<LeaderboardEntry> result = new ArrayList<>(items.size());

        int rank = page.getNumber() * page.getSize() + 1;
        for (StudentTest st : items) {
            User u = st.getUser(); // user is pre-fetched via @EntityGraph on repository method
            result.add(new LeaderboardEntry(
                    rank++,
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    st.getScore()
            ));
        }
        return result;
    }
}
