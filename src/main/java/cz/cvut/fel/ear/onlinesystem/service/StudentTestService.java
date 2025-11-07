package cz.cvut.fel.ear.onlinesystem.service;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;

import java.util.List;

public interface StudentTestService {
    TestStats computeStatsForTest(int testId);

    List<LeaderboardEntry> topN(int testId, int n);
    List<LeaderboardEntry> lowN(int testId, int n);
}
