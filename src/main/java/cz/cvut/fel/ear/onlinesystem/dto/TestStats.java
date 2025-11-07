package cz.cvut.fel.ear.onlinesystem.dto;

import java.util.List;

public record TestStats(
        int testId,
        long attempts,
        long distinctStudents,
        Integer maxPoints, // Test.maxPoints
        Integer sumQuestionPoints,
        Double averageScore,
        Integer minScore,
        Integer medianScore,
        Integer p90,
        Integer maxScore,
        List<StatusBucket> statusHistogram
) {
}
