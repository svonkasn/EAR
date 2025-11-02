package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Integer> {
    List<Test> findByTerminId(Integer terminId);
    List<Test> findByGroupId(Integer groupId);
    @Query("select t from Test t where t.termin.id = :terminId and t.group.id = :groupId")
    Optional<Test> findByTerminAndGroup(@Param("terminId") Integer terminId,
                                        @Param("groupId") Integer groupId);
}
