package cz.cvut.fel.ear.onlinesystem.dto.request;

public record StudentTestCreateRequest(
        int userId,
        int testId
) { }
