package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.mapper.QuestionMapper;
import cz.cvut.fel.ear.onlinesystem.dto.request.MultipleChoiceCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.OpenQuestionCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.QuestionResponse;
import cz.cvut.fel.ear.onlinesystem.service.QuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/tests/{testId}/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionMapper questionMapper;

    public QuestionController(QuestionService questionService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
    }

    // LIST questions
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping
    public List<QuestionResponse> list(@PathVariable int testId) {
        return questionService.list(testId).stream()
                .map(questionMapper::toDto)
                .toList();
    }

    // CREATE OPEN
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping("/open/create")
    public QuestionResponse addOpen(@PathVariable int testId,
                                    @RequestBody OpenQuestionCreateRequest req) {
        return questionMapper.toDto(questionService.addOpen(testId, req));
    }

    // CREATE MCQ
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping("/mcq/create")
    public QuestionResponse addMcq(@PathVariable int testId,
                                   @RequestBody MultipleChoiceCreateRequest req) {
        return questionMapper.toDto(questionService.addMcq(testId, req));
    }

    // UPDATE OPEN
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PatchMapping("/{questionId}/open/update")
    public QuestionResponse updateOpen(@PathVariable int testId,
                                       @PathVariable int questionId,
                                       @RequestBody OpenQuestionCreateRequest req) {
        return questionMapper.toDto(questionService.updateOpen(testId, questionId, req));
    }

    // UPDATE MCQ
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PatchMapping("/{questionId}/mcq/update")
    public QuestionResponse updateMcq(@PathVariable int testId,
                                      @PathVariable int questionId,
                                      @RequestBody MultipleChoiceCreateRequest req) {
        return questionMapper.toDto(questionService.updateMcq(testId, questionId, req));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping("/{questionId}/delete")
    public void delete(@PathVariable int testId, @PathVariable int questionId) {
        questionService.delete(testId, questionId);
    }
}

