package cz.cvut.fel.ear.onlinesystem.dto.mapper;

import cz.cvut.fel.ear.onlinesystem.dto.response.TestResponse;
import cz.cvut.fel.ear.onlinesystem.model.Question;
import cz.cvut.fel.ear.onlinesystem.model.Test;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestMapper {

    private TestMapper() {}

    public TestResponse toDto(Test t) {
        List<Integer> qIds = t.getQuestions() == null ? List.of() :
                t.getQuestions().stream()
                        .map(Question::getId)
                        .toList();

        return new TestResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getMaxPoints(),
                t.getMaxAttempts(),
                t.getTeacher() == null ? null : t.getTeacher().getId(),
                t.getCourse() == null ? null : t.getCourse().getId(),
                t.getGroup() == null ? null : t.getGroup().getId(),
                t.getTermin() == null ? null : t.getTermin().getId(),
                qIds
        );
    }

}

