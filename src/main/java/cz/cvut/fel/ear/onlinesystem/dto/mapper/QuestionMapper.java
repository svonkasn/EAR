package cz.cvut.fel.ear.onlinesystem.dto.mapper;

import cz.cvut.fel.ear.onlinesystem.dto.response.QuestionResponse;
import cz.cvut.fel.ear.onlinesystem.model.MultipleChoice;
import cz.cvut.fel.ear.onlinesystem.model.OpenQuestion;
import cz.cvut.fel.ear.onlinesystem.model.Question;
import org.springframework.stereotype.Component;

@Component
public class QuestionMapper {

    private QuestionMapper() {}

    public QuestionResponse toDto(Question q) {
        if (q instanceof OpenQuestion oq) {
            return new QuestionResponse(
                    oq.getId(),
                    "OPEN",
                    oq.getPoint(),
                    oq.getQuestionText(),
                    oq.getExpectedAnswer(),
                    oq.getMaxLength(),
                    null, null, null, null,
                    null, null, null, null,
                    false
            );
        }
        if (q instanceof MultipleChoice mcq) {
            return new QuestionResponse(
                    mcq.getId(),
                    "MCQ",
                    mcq.getPoint(),
                    mcq.getQuestionText(),
                    null,
                    null,
                    mcq.getOptionA(),
                    mcq.getOptionB(),
                    mcq.getOptionC(),
                    mcq.getOptionD(),
                    mcq.getCorrectA(),
                    mcq.getCorrectB(),
                    mcq.getCorrectC(),
                    mcq.getCorrectD(),
                    mcq.isMultipleCorrect()
            );
        }

        // base question
        return new QuestionResponse(
                q.getId(),
                "QUESTION",
                q.getPoint(),
                q.getQuestionText(),
                null, null,
                null, null, null, null,
                null, null, null, null,
                false
        );
    }

}

