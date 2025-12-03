package cz.cvut.fel.ear.onlinesystem.dto.response;

public record StudentTestResponse(
  int id,
  Integer score,
  String status,
  int userId,
  int testId
) {}
