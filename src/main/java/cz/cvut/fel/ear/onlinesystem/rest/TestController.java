package cz.cvut.fel.ear.onlinesystem.rest;

import cz.cvut.fel.ear.onlinesystem.dto.mapper.TestMapper;
import cz.cvut.fel.ear.onlinesystem.dto.request.TestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.TestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.response.TestResponse;
import cz.cvut.fel.ear.onlinesystem.model.Test;
import cz.cvut.fel.ear.onlinesystem.service.TestService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/tests")
public class TestController {

    private final TestService testService;
    private final TestMapper testMapper;

    public TestController(TestService testService, TestMapper testMapper) {
        this.testService = testService;
        this.testMapper = testMapper;
    }

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PostMapping("/create")
    public TestResponse create(@RequestBody TestCreateRequest req) {
        Test created = testService.create(req);
        return testMapper.toDto(created);
    }

    // READ
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping("/{testId}")
    public TestResponse get(@PathVariable int testId) {
        return testMapper.toDto(testService.get(testId));
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @PutMapping("/{testId}/update")
    public TestResponse update(@PathVariable int testId, @RequestBody TestUpdateRequest req) {
        return testMapper.toDto(testService.update(testId, req));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @DeleteMapping("/{testId}/delete")
    public void delete(@PathVariable int testId) {
        testService.delete(testId);
    }
}

