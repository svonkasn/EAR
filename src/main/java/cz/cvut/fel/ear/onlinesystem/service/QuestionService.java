package cz.cvut.fel.ear.onlinesystem.service;

import cz.cvut.fel.ear.onlinesystem.dto.request.MultipleChoiceCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.OpenQuestionCreateRequest;
import cz.cvut.fel.ear.onlinesystem.model.Question;

import java.util.List;

public interface QuestionService {
    Question addOpen(int testId, OpenQuestionCreateRequest req);
    Question addMcq(int testId, MultipleChoiceCreateRequest req);

    Question updateOpen(int testId, int questionId, OpenQuestionCreateRequest req);
    Question updateMcq(int testId, int questionId, MultipleChoiceCreateRequest req);

    void delete(int testId, int questionId);
    List<Question> list(int testId);
}

