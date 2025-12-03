package cz.cvut.fel.ear.onlinesystem.dto.mapper;

import cz.cvut.fel.ear.onlinesystem.dto.response.StudentAnswerResponse;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import org.springframework.stereotype.Component;

@Component
public class StudentAnswerMapper {

  public StudentAnswerResponse toDto(StudentAnswer a) {
    return new StudentAnswerResponse(
      a.getId(),
      a.getSelectedA(),
      a.getSelectedB(),
      a.getSelectedC(),
      a.getSelectedD(),
      a.getAwardedPoints(),
      a.getCorrect(),
      a.getTeacherNote()
    );
  }
}
