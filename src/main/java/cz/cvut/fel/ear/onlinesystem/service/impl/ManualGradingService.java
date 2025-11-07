package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.exception.ValidationException;
import cz.cvut.fel.ear.onlinesystem.model.OpenQuestion;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ManualGradingService {
    private final StudentAnswerRepository answerRepo;
    private final StudentTestRepository stRepo;
    private final AutoGraderService auto;

    public ManualGradingService(StudentAnswerRepository answerRepo, StudentTestRepository stRepo, AutoGraderService auto) {
        this.answerRepo = answerRepo;
        this.stRepo = stRepo;
        this.auto = auto;
    }

    @Transactional
    public void gradeOpenAnswer(int answerId, int points, int teacherId, String note) {
        StudentAnswer a = answerRepo.findById(answerId)
                .orElseThrow(() -> new NotFoundException("Answer " + answerId + " not found"));

        if (!(a.getQuestion() instanceof OpenQuestion)) {
            throw new ValidationException("Only OpenQuestion can be graded manually");
        }
        Integer max = a.getQuestion().getPoint();
        if (max == null) throw new ValidationException("Question has no max points");
        if (points < 0 || points > max) throw new ValidationException("Points out of range 0.." + max);

        a.setAwardedPoints(points);
        a.setCorrect(Objects.equals(points, max));
        a.setTeacherNote(note);
        answerRepo.save(a);

        auto.recomputeTotalScore(a.getStudentTest().getId());
    }

    @Transactional
    public void finalizeIfAllGraded(int studentTestId) {
        long pending = answerRepo.countOpenNotGraded(studentTestId);
        if (pending == 0L) {
            StudentTest st = stRepo.findById(studentTestId)
                    .orElseThrow(() -> new NotFoundException("Attempt " + studentTestId + " not found"));
            st.setStatus(StatusTest.EVALUATED);
        }
    }
}
