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
        Test test = testRepo.findById(testId).orElseThrow(()-> new NotFoundException("Test with id " + testId + " not found"));
        long attempts = studentTestRepo.attemptsCount(testId);
        long distinct = studentTestRepo.distinctStudents(testId);
        Double avg = studentTestRepo.averageScore(testId);
        Integer min = studentTestRepo.minScore(testId);
        Integer max = studentTestRepo.maxScore(testId);

        List<StatusBucket> buckets = studentTestRepo.statusHistogram(testId).stream()
                .map(v -> new StatusBucket(v.getStatus(), v.getCount())).toList();

        Integer sumPoints = questionRepo.sumPoints(testId);

        long graded = studentTestRepo.countByTestIdAndScoreIsNotNull(testId);
        List<Integer> sorted = graded == 0 ? List.of() : studentTestRepo.scoresSorted(testId);
        Integer median = median(sorted);
        Integer p90 = percentile(sorted, 90);

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

    private static Integer median(List<Integer> sorted) {
        if (sorted == null || sorted.isEmpty()) return null;
        int n = sorted.size();
        if ((n & 1) == 1) return sorted.get(n / 2);
        return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2;
    }

    private static Integer percentile(List<Integer> sorted, int p) {
        if (sorted == null || sorted.isEmpty()) return null;
        int n = sorted.size();
        int rank = (int) Math.ceil((p / 100.0) * n);
        rank = Math.max(1, Math.min(rank, n));
        return sorted.get(rank - 1);
    }

    private static List<LeaderboardEntry> toLeaderboard(Page<StudentTest> page) {
        List<StudentTest> items = page.getContent();
        List<LeaderboardEntry> result = new ArrayList<>(items.size());

        int startRank = page.getNumber() * page.getSize() + 1;

        int rank = startRank;
        for (StudentTest st : items) {
            User u = st.getUser();
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
