package cz.cvut.fel.ear.onlinesystem.dto.mapper;

import cz.cvut.fel.ear.onlinesystem.dto.response.StudentTestResponse;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import org.springframework.stereotype.Component;

@Component
public class StudentTestMapper {

  public StudentTestResponse toDto(StudentTest st) {
    return new StudentTestResponse(
      st.getId(),
      st.getScore(),
      st.getStatus().name(),
      st.getUser().getId(),
      st.getTest().getId()
    );
  }
}
