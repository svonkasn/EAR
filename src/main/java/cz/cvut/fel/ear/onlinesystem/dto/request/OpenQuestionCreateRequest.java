package cz.cvut.fel.ear.onlinesystem.dto.request;

public record OpenQuestionCreateRequest(
        Integer point,
        String questionText,
        String expectedAnswer,
        Integer maxLength
) {
}

