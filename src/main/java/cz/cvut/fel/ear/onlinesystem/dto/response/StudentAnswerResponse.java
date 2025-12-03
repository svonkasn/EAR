package cz.cvut.fel.ear.onlinesystem.dto.response;

public record StudentAnswerResponse(int id,
                                    Boolean selA,
                                    Boolean selB,
                                    Boolean selC,
                                    Boolean selD,
                                    Integer awardedPoints,
                                    Boolean correct,
                                    String teacherNote) {
}
