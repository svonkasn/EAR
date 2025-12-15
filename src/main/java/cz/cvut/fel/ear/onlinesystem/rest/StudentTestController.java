package cz.cvut.fel.ear.onlinesystem.rest;


import cz.cvut.fel.ear.onlinesystem.dto.mapper.StudentTestMapper;
import cz.cvut.fel.ear.onlinesystem.dto.request.AttemptSubmitRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.StudentTestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.StudentTestResponse;
import cz.cvut.fel.ear.onlinesystem.model.*;
import cz.cvut.fel.ear.onlinesystem.service.StudentTestService;
import cz.cvut.fel.ear.onlinesystem.service.security.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/attempts")
public class StudentTestController {
  private final StudentTestService studentTestService;
  private final StudentTestMapper studentTestMapper;
  private final SecurityService securityService;

  public StudentTestController(StudentTestService studentTestService,
                               StudentTestMapper studentTestMapper,
                               SecurityService securityService) {
    this.studentTestService = studentTestService;
    this.studentTestMapper = studentTestMapper;
    this.securityService = securityService;
  }


  /* CREATE */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and #req.userId == @securityService.getCurrentUserId())")
  @PostMapping("/create")
  public StudentTestResponse createAttempt(@RequestBody StudentTestCreateRequest req) {
    StudentTest created = studentTestService.create(req);
    return studentTestMapper.toDto(created);
  }

  /* READ ONE */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @securityService.isOwnedAttempt(#id))")
  @GetMapping("/{id}")
  public StudentTestResponse getAttempt(@PathVariable int id) {
    StudentTest st = studentTestService.get(id);
    return studentTestMapper.toDto(st);
  }

  /* READ LIST */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @GetMapping
  public List<StudentTestResponse> listAttempts(
          @RequestParam(required = false) Integer userId,
          @RequestParam(required = false) Integer testId
  ) {
    return studentTestService.listAttempts(userId, testId).stream()
            .map(studentTestMapper::toDto)
            .toList();
  }

  @PreAuthorize("hasRole('STUDENT')")
  @GetMapping("/my")
  public List<StudentTestResponse> myAttempts(@RequestParam Integer testId) {
    return studentTestService.listAttemptsForStudents(testId).stream()
            .map(studentTestMapper::toDto)
            .toList();
  }

  /* UPDATE */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @PutMapping("/{id}/update")
  public StudentTestResponse updateAttempt(@PathVariable int id,
                                           @RequestBody StudentTestUpdateRequest req) {
    StudentTest st = studentTestService.update(id, req);
    return studentTestMapper.toDto(st);
  }

  /* DELETE */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}/delete")
  public ResponseEntity<Void> deleteAttempt(@PathVariable int id) {
    studentTestService.delete(id);
    return ResponseEntity.noContent().build();
  }

  /* SUBMIT */
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or (hasRole('STUDENT') and @securityService.isOwnedAttempt(#attemptId))")
  @PostMapping("/{attemptId}/submit")
  public ResponseEntity<Void> submit(@PathVariable int attemptId, @RequestBody AttemptSubmitRequest request) {
    studentTestService.submitAttempt(attemptId, request.userId());
    return ResponseEntity.ok().build();
  }

}
