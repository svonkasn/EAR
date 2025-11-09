package cz.cvut.fel.ear.onlinesystem.service;

import cz.cvut.fel.ear.onlinesystem.dto.LeaderboardEntry;
import cz.cvut.fel.ear.onlinesystem.dto.TestStats;

import java.util.List;

/**
 * Read-only statistics service for tests.
 * Aggregates metrics per test and produces leaderboards.
 */
public interface StudentTestService {
    /**
     * Compute all summary statistics for a given test.
     * Includes attempts/unique students, avg/min/median/p90/max, status histogram,
     * test configuration (maxPoints) and sum of question points.
     *
     * @param testId target test id
     * @return aggregated stats DTO
     */
    TestStats computeStatsForTest(int testId);

    /**
     * Top-N leaderboard (highest scores first), graded attempts only.
     */
    List<LeaderboardEntry> topN(int testId, int n);

    /**
     * Low-N leaderboard (lowest scores first), graded attempts only.
     */
    List<LeaderboardEntry> lowN(int testId, int n);
}
