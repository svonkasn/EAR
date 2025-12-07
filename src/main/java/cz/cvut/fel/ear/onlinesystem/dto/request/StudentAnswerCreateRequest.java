package cz.cvut.fel.ear.onlinesystem.dto.request;

public record StudentAnswerCreateRequest(
        int studentTestId,
        int questionId,
        Boolean selA,
        Boolean selB,
        Boolean selC,
        Boolean selD,
        String text
) { }
