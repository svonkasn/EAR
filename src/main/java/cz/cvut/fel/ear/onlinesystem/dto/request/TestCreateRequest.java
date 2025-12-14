package cz.cvut.fel.ear.onlinesystem.dto.request;

public record TestCreateRequest(
        String title,
        String description,
        Integer maxAttempts,
        Integer teacherId,
        Integer courseId,
        Integer groupId,
        Integer terminId
) {
}

