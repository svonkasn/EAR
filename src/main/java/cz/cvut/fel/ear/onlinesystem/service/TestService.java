package cz.cvut.fel.ear.onlinesystem.service;

import cz.cvut.fel.ear.onlinesystem.dto.request.TestCreateRequest;
import cz.cvut.fel.ear.onlinesystem.dto.request.TestUpdateRequest;
import cz.cvut.fel.ear.onlinesystem.model.Test;

import java.util.List;

public interface TestService {

    Test create(TestCreateRequest req);
    Test update(int testId, TestUpdateRequest req);
    Test get(int testId);
    void delete(int testId);

    List<Test> findByTermin(int terminId);
    List<Test> findByGroup(int groupId);

}

