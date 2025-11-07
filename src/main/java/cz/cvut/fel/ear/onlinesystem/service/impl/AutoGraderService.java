package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.MultipleChoice;
import cz.cvut.fel.ear.onlinesystem.model.Question;
import cz.cvut.fel.ear.onlinesystem.model.StudentAnswer;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class AutoGraderService {
    private final StudentAnswerRepository answerRepo;
    private final StudentTestRepository stRepo;

    public AutoGraderService(StudentAnswerRepository answerRepo, StudentTestRepository stRepo) {
        this.answerRepo = answerRepo;
        this.stRepo = stRepo;
    }

    @Transactional
    public void gradeClosedQuestions(int studentTestId) {
        List<StudentAnswer> answers = answerRepo.findByStudentTestId(studentTestId);
        for (StudentAnswer a : answers) {
            Question q = a.getQuestion();
            if (q instanceof MultipleChoice mc) {
                boolean ok = mc.matches(a.getSelectedA(), a.getSelectedB(), a.getSelectedC(), a.getSelectedD());
                a.setCorrect(ok);
                a.setAwardedPoints(ok ? mc.getPoint() : 0);
            }
        }
        answerRepo.saveAll(answers);
    }

    @Transactional
    public int recomputeTotalScore(int studentTestId) {
        StudentTest st = stRepo.findById(studentTestId)
                .orElseThrow(() -> new NotFoundException("Attempt " + studentTestId + " not found"));
        List<StudentAnswer> answers = answerRepo.findByStudentTestId(studentTestId);

        int total = answers.stream().map(StudentAnswer::getAwardedPoints).filter(Objects::nonNull).mapToInt(Integer::intValue).sum();

        Integer cap = st.getTest().getMaxPoints();
        if (cap != null) total = Math.min(total, cap);
        st.setScore(total);
        return total;
    }

}
