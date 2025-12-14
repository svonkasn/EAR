package cz.cvut.fel.ear.onlinesystem.dto.request;

public record TestUpdateRequest(
        String title,
        String description,
        Integer maxAttempts,
        Integer courseId,
        Integer groupId,
        Integer terminId
) {
}

