package cz.cvut.fel.ear.onlinesystem.dto.request;

public record MultipleChoiceCreateRequest(
        Integer point,
        String questionText,
        String optionA,
        String optionB,
        String optionC,
        String optionD,
        Boolean correctA,
        Boolean correctB,
        Boolean correctC,
        Boolean correctD,
        boolean multipleCorrect
) {
}

