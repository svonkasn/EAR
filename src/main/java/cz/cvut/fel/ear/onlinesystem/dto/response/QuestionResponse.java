package cz.cvut.fel.ear.onlinesystem.dto.response;

public record QuestionResponse(
        Integer id,
        String type,
        Integer point,
        String questionText,

        // OPEN
        String expectedAnswer,
        Integer maxLength,

        // MCQ
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        Boolean correctA,
        Boolean correctB,
        Boolean correctC,
        Boolean correctD,
        Boolean multipleCorrect
) {
}

