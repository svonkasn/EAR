package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.exception.ConflictException;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.service.AttemptService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final StudentTestRepository stRepo;
    private final StudentAnswerRepository answerRepo;
    private final AutoGraderService autoGraderService;

    public AttemptServiceImpl(StudentTestRepository stRepo, StudentAnswerRepository answerRepo, AutoGraderService autoGraderService) {
        this.stRepo = stRepo;
        this.answerRepo = answerRepo;
        this.autoGraderService = autoGraderService;
    }

    @Override
    public void submitAttempt(int studentTestId, int currentUserId) {
        StudentTest st = stRepo.findById(studentTestId)
                .orElseThrow(() -> new NotFoundException("Attempt " + studentTestId + " not found"));
        if (st.getUser() != null && st.getUser().getId() != null
                && !st.getUser().getId().equals(currentUserId)) {
            throw new ConflictException("You cannot submit someone else's attempt");
        }
        if (st.getStatus() != StatusTest.IN_PROGRESS) {
            throw new ConflictException("Attempt is not in progress");
        }
        autoGraderService.gradeClosedQuestions(studentTestId);
        autoGraderService.recomputeTotalScore(studentTestId);

        long pending = answerRepo.countOpenNotGraded(studentTestId);
        st.setStatus(pending == 0 ? StatusTest.EVALUATED : StatusTest.SUBMITTED);
    }
}
