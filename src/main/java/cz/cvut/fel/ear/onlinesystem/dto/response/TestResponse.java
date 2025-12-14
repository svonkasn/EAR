package cz.cvut.fel.ear.onlinesystem.dto.response;

import java.util.List;

public record TestResponse(
        Integer id,
        String title,
        String description,
        Integer maxPoints,
        Integer maxAttempts,
        Integer teacherId,
        Integer courseId,
        Integer groupId,
        Integer terminId,
        List<Integer> questionIds
) {
}

