package cz.cvut.fel.ear.onlinesystem.service;

import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;

import java.util.List;

/**
 * Orchestrates the "submit attempt" use-case.
 * Performs ownership/security checks, triggers auto-grading for closed questions,
 * recomputes total score and updates attempt status.
 */
public interface StudentTestService {
    /**
     * Submit an in-progress attempt.
     * <p>
     * Steps:
     * 1) Owner check (student can submit only their own attempt);
     * 2) Auto-grade closed questions;
     * 3) Recompute total score;
     * 4) Set status to SUBMITTED or EVALUATED (if no open answers need grading).
     *
     * @param studentTestId attempt id
     * @param currentUserId id of the authenticated user performing the action
     */
    void submitAttempt(int studentTestId, int currentUserId);
    StudentTest create(StudentTestCreateRequest req);
    StudentTest get(int id);
    StudentTest update(int id, StudentTestUpdateRequest req);
    void delete(int id);

    List<StudentTest> listAttemptsForStudents(Integer testId);
    List<StudentTest> listAttempts(Integer userId, Integer testId);

}
