package cz.cvut.fel.ear.onlinesystem.repository;

import cz.cvut.fel.ear.onlinesystem.model.StatusTest;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentTestRepository extends JpaRepository<StudentTest, Integer> {
    @Query("select avg(st.score) from StudentTest st where st.test.id = :testId and st.score is not null")
    Double averageScore(@Param("testId") Integer testId);

    @Query("select count(st) from StudentTest st where st.test.id = :testId")
    long attemptsCount(@Param("testId") Integer testId);

    @Query("select count(distinct st.user.id) from StudentTest st where st.test.id = :testId")
    long distinctStudents(@Param("testId") Integer testId);

    @Query("select st.score from StudentTest st where st.test.id = :testId and st.score is not null order by st.score asc")
    List<Double> scoresSorted(@Param("testId") Integer testId);

    @Query("""
           select st.status as status, count(st) as count
           from StudentTest st
           where st.test.id = :testId
           group by st.status
           """)
    List<StatusCountView> statusHistogram(@Param("testId") Integer testId);

    interface StatusCountView {
        StatusTest getStatus();
        long getCount();
    }

    // get a specific user attempt on a test
    Optional<StudentTest> findByUserIdAndTestId(Integer userId, Integer testId);

    // number of attempts
    long countByUserIdAndTestId(Integer userId, Integer testId);

    // top N students
    @EntityGraph(attributePaths = "user")
    Page<StudentTest> findByTestIdAndScoreIsNotNull(Integer testId, Pageable pageable);

    public static Pageable topN(int n) { return PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "score")); }
    public static Pageable lowN(int n) { return PageRequest.of(0, n, Sort.by(Sort.Direction.ASC,  "score")); }

    long countByTestIdAndScoreIsNotNull(Integer testId);

    @Query("select min(st.score) from StudentTest st where st.test.id = :testId and st.score is not null")
    Integer minScore(@Param("testId") Integer testId);

    @Query("select max(st.score) from StudentTest st where st.test.id = :testId and st.score is not null")
    Integer maxScore(@Param("testId") Integer testId);
}
