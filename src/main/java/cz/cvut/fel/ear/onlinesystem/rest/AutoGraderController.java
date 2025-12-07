package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.mapper.StudentTestMapper;
import cz.cvut.fel.ear.onlinesystem.dto.response.StudentTestResponse;
import cz.cvut.fel.ear.onlinesystem.exception.NotFoundException;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.service.impl.AutoGraderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/autograder")
public class AutoGraderController {

    private final AutoGraderService autoGraderService;
    private final StudentTestRepository stRepo;
    private final StudentTestMapper studentTestMapper;

    public AutoGraderController(AutoGraderService autoGraderService,
                                StudentTestRepository stRepo,
                                StudentTestMapper studentTestMapper) {
        this.autoGraderService = autoGraderService;
        this.stRepo = stRepo;
        this.studentTestMapper = studentTestMapper;
    }

    @PostMapping("/{attemptId}/grade-closed")
    public StudentTestResponse gradeClosed(@PathVariable int attemptId) {
        autoGraderService.gradeClosedQuestions(attemptId);
        autoGraderService.recomputeTotalScore(attemptId);

        StudentTest st = stRepo.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        return studentTestMapper.toDto(st);
    }

    @PostMapping("/{attemptId}/recompute-score")
    public StudentTestResponse recomputeScore(@PathVariable int attemptId) {
        autoGraderService.recomputeTotalScore(attemptId);
        StudentTest st = stRepo.findById(attemptId)
                .orElseThrow(() -> new NotFoundException("Attempt not found"));
        return studentTestMapper.toDto(st);
    }
}
