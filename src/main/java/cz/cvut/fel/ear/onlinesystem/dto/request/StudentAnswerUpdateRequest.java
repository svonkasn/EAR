package cz.cvut.fel.ear.onlinesystem.dto.request;

public record StudentAnswerUpdateRequest(
        Boolean selA,
        Boolean selB,
        Boolean selC,
        Boolean selD,
        String text
) { }
