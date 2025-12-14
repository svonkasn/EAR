package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.dto.request.MultipleChoiceCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.OpenQuestionCreateRequest;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.MultipleChoice;
import cz.cvut.fel.ear.onlinesystem.model.OpenQuestion;
import cz.cvut.fel.ear.onlinesystem.model.Question;
import cz.cvut.fel.ear.onlinesystem.model.Test;
import cz.cvut.fel.ear.onlinesystem.repository.QuestionRepository;
import cz.cvut.fel.ear.onlinesystem.repository.TestRepository;
import cz.cvut.fel.ear.onlinesystem.service.QuestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final TestRepository testRepo;
    private final QuestionRepository questionRepo;

    public QuestionServiceImpl(TestRepository testRepo, QuestionRepository questionRepo) {
        this.testRepo = testRepo;
        this.questionRepo = questionRepo;
    }

    @Override
    public Question addOpen(int testId, OpenQuestionCreateRequest req) {
        Test test = testRepo.findWithQuestionsById(testId)
                .orElseThrow(() -> new NotFoundException("Test with id " + testId + " not found"));

        OpenQuestion q = new OpenQuestion();
        q.setPoint(req.point());
        q.setQuestionText(req.questionText());
        q.setExpectedAnswer(req.expectedAnswer());
        q.setMaxLength(req.maxLength());
        q.setQuestionOrder(test.getQuestions().size() + 1);

        attach(test, q);
        recalcMaxPoints(test);

        // Cascade ALL on Test.questions will persist q if we save test.
        testRepo.save(test);
        return q;
    }

    @Override
    public Question addMcq(int testId, MultipleChoiceCreateRequest req) {
        Test test = testRepo.findWithQuestionsById(testId)
                .orElseThrow(() -> new NotFoundException("Test with id " + testId + " not found"));

        MultipleChoice q = new MultipleChoice();
        q.setPoint(req.point());
        q.setQuestionText(req.questionText());

        q.setOptionA(req.optionA());
        q.setOptionB(req.optionB());
        q.setOptionC(req.optionC());
        q.setOptionD(req.optionD());

        q.setCorrectA(req.correctA());
        q.setCorrectB(req.correctB());
        q.setCorrectC(req.correctC());
        q.setCorrectD(req.correctD());

        q.setMultipleCorrect(req.multipleCorrect());

        q.setQuestionOrder(test.getQuestions().size() + 1);

        attach(test, q);
        recalcMaxPoints(test);

        testRepo.save(test);
        return q;
    }

    @Override
    public Question updateOpen(int testId, int questionId, OpenQuestionCreateRequest req) {
        Question base = questionRepo.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));

        ensureBelongsToTest(base, testId);
        if (!(base instanceof OpenQuestion q)) {
            throw new IllegalArgumentException("Question " + questionId + " is not OpenQuestion");
        }

        if (req.point() != null) q.setPoint(req.point());
        if (req.questionText() != null) q.setQuestionText(req.questionText());
        q.setExpectedAnswer(req.expectedAnswer());
        q.setMaxLength(req.maxLength());

        Test test = base.getTest();
        recalcMaxPoints(test);

        return questionRepo.save(q);
    }

    @Override
    public Question updateMcq(int testId, int questionId, MultipleChoiceCreateRequest req) {
        Question base = questionRepo.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));

        ensureBelongsToTest(base, testId);
        if (!(base instanceof MultipleChoice q)) {
            throw new IllegalArgumentException("Question " + questionId + " is not MultipleChoice");
        }

        if (req.point() != null) q.setPoint(req.point());
        if (req.questionText() != null) q.setQuestionText(req.questionText());

        q.setOptionA(req.optionA());
        q.setOptionB(req.optionB());
        q.setOptionC(req.optionC());
        q.setOptionD(req.optionD());

        q.setCorrectA(req.correctA());
        q.setCorrectB(req.correctB());
        q.setCorrectC(req.correctC());
        q.setCorrectD(req.correctD());

        q.setMultipleCorrect(req.multipleCorrect());

        Test test = base.getTest();
        recalcMaxPoints(test);

        return questionRepo.save(q);
    }

    @Override
    public void delete(int testId, int questionId) {
        Test test = testRepo.findWithQuestionsById(testId)
                .orElseThrow(() -> new NotFoundException("Test with id " + testId + " not found"));

        Question q = questionRepo.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question with id " + questionId + " not found"));

        ensureBelongsToTest(q, testId);

        test.getQuestions().removeIf(x -> x.getId().equals(q.getId()));
        recalcMaxPoints(test);

        testRepo.save(test);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Question> list(int testId) {
        if (!testRepo.existsById(testId)) {
            throw new NotFoundException("Test with id " + testId + " not found");
        }
        return questionRepo.findByTestId(testId);
    }

    private static void attach(Test test, Question q) {
        q.setTest(test);
        test.getQuestions().add(q);
    }

    private static void ensureBelongsToTest(Question q, int testId) {
        if (q.getTest() == null || q.getTest().getId() == null || q.getTest().getId() != testId) {
            throw new NotFoundException("Question does not belong to test " + testId);
        }
    }

    /** Recalculate and store test.maxPoints based on its question points. */
    private static void recalcMaxPoints(Test test) {
        int sum = test.getQuestions().stream()
                .map(Question::getPoint)
                .filter(p -> p != null)
                .mapToInt(Integer::intValue)
                .sum();
        test.setMaxPoints(sum);
    }
}


