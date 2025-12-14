package cz.cvut.fel.ear.onlinesystem.service.impl;

import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.exception.ConflictException;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.model.Test;
import cz.cvut.fel.ear.onlinesystem.model.User;
import cz.cvut.fel.ear.onlinesystem.repository.StudentAnswerRepository;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.TestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.UserRepository;
import cz.cvut.fel.ear.onlinesystem.service.StudentTestService;
import cz.cvut.fel.ear.onlinesystem.service.security.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentTestServiceImpl implements StudentTestService {

    private final StudentTestRepository studentTestRepository;
    private final StudentAnswerRepository answerRepo;
    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final SecurityService securityService;
    private final AutoGraderService autoGraderService;

    public StudentTestServiceImpl(StudentTestRepository studentTestRepository, StudentAnswerRepository answerRepo, AutoGraderService autoGraderService,
                                  UserRepository userRepository, TestRepository testRepository, SecurityService securityService) {
        this.studentTestRepository = studentTestRepository;
        this.answerRepo = answerRepo;
        this.autoGraderService = autoGraderService;
        this.userRepository = userRepository;
        this.testRepository = testRepository;
        this.securityService = securityService;
    }

    @Override
    public void submitAttempt(int studentTestId, int currentUserId) {
        // Load attempt or fail fast
        StudentTest st = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new NotFoundException("Attempt " + studentTestId + " not found"));

        // Ownership check: only owner (or admin via higher layer) can submit the attempt
        if (st.getUser() != null && st.getUser().getId() != null
                && !st.getUser().getId().equals(currentUserId)) {
            throw new ConflictException("You cannot submit someone else's attempt");
        }

        // Business state check
        if (st.getStatus() != StatusTest.IN_PROGRESS) {
            throw new ConflictException("Attempt is not in progress");
        }

        // 1) Auto-grade closed questions (MultipleChoice)
        autoGraderService.gradeClosedQuestions(studentTestId);

        // 2) Recompute the total score based on awarded points
        autoGraderService.recomputeTotalScore(studentTestId);

        // 3) If there are no open answers waiting -> finalize; otherwise mark as SUBMITTED
        long pending = answerRepo.countOpenNotGraded(studentTestId);
        st.setStatus(pending == 0 ? StatusTest.EVALUATED : StatusTest.SUBMITTED);
    }

    @Override
    public StudentTest create(StudentTestCreateRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        Test test = testRepository.findById(req.testId())
                .orElseThrow(() -> new NotFoundException("Test not found"));

        StudentTest st = new StudentTest();
        st.setUser(user);
        st.setTest(test);
        st.setStatus(StatusTest.IN_PROGRESS);
        st.setScore(null);

        return studentTestRepository.save(st);
    }

    @Override
    public StudentTest get(int id) {
        return studentTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
    }

    @Override
    public StudentTest update(int id, StudentTestUpdateRequest req) {
        StudentTest st = studentTestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));

        if (req.status() != null) {
            st.setStatus(StatusTest.valueOf(req.status()));
        }
        return studentTestRepository.save(st);
    }

    @Override
    public void delete(int id) {
        if (!studentTestRepository.existsById(id)) {
            throw new NotFoundException("Attempt not found");
        }
        studentTestRepository.deleteById(id);
    }

    @Override
    public List<StudentTest> listAttemptsForStudents(Integer userId, Integer testId) {
        List<StudentTest> attempts;

        Integer currentUserId = securityService.getCurrentUserId();

        if (testId != null) {
            attempts = studentTestRepository.findByUserIdAndTestId(currentUserId, testId);
        }  else {
            attempts = studentTestRepository.findByUserId(currentUserId);
        }

        return attempts;
    }

    @Override
    public List<StudentTest> listAttempts(Integer userId, Integer testId) {

        List<StudentTest> attempts;

        if (userId != null && testId != null) {
            attempts = studentTestRepository.findByUserIdAndTestId(userId, testId);
        } else if (userId != null) {
            attempts = studentTestRepository.findByUserId(userId);
        } else if (testId != null) {
            attempts = studentTestRepository.findByTestId(testId);
        } else {
            attempts = studentTestRepository.findAll();
        }

        return attempts;

    }

}
