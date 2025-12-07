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
    /** Average score among graded attempts (score is not null). */
    @Query("select avg(st.score) from StudentTest st where st.test.id = :testId and st.score is not null")
    Double averageScore(@Param("testId") Integer testId);

    /** Total number of attempts (graded + not graded). */
    @Query("select count(st) from StudentTest st where st.test.id = :testId")
    long attemptsCount(@Param("testId") Integer testId);

    /** How many unique students attempted the test. */
    @Query("select count(distinct st.user.id) from StudentTest st where st.test.id = :testId")
    long distinctStudents(@Param("testId") Integer testId);

    /** Sorted scores (ascending) for percentile/median calculations; excludes null scores. */
    @Query("select st.score from StudentTest st where st.test.id = :testId and st.score is not null order by st.score asc")
    List<Integer> scoresSorted(@Param("testId") Integer testId);

    /** Distribution by attempt status (NOT_STARTED/IN_PROGRESS/SUBMITTED/EVALUATED). */
    @Query("""
           select st.status as status, count(st) as count
           from StudentTest st
           where st.test.id = :testId
           group by st.status
           """)
    List<StatusCountView> statusHistogram(@Param("testId") Integer testId);

    /** Closed projection for the status histogram row. */
    interface StatusCountView {
        StatusTest getStatus();
        long getCount();
    }

    /** Single user's attempt for a given test (if such attempt exists). */
    List<StudentTest> findByUserIdAndTestId(Integer userId, Integer testId);

    /** Number of attempts made by a given user for a given test (useful for maxAttempts checks). */
    long countByUserIdAndTestId(Integer userId, Integer testId);

    /**
     * Leaderboard source: fetch graded attempts with user pre-fetched to avoid N+1.
     * Provide a Pageable with sort by score (desc/asc) and size = N.
     */
    @EntityGraph(attributePaths = "user")
    Page<StudentTest> findByTestIdAndScoreIsNotNull(Integer testId, Pageable pageable);

    /** Convenience pageable for Top-N (highest scores first). */
    static Pageable topN(int n) { return PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "score")); }

    /** Convenience pageable for Low-N (lowest scores first). */
    static Pageable lowN(int n) { return PageRequest.of(0, n, Sort.by(Sort.Direction.ASC,  "score")); }

    /** How many graded attempts exist for a test. */
    long countByTestIdAndScoreIsNotNull(Integer testId);

    /** Minimum graded score. */
    @Query("select min(st.score) from StudentTest st where st.test.id = :testId and st.score is not null")
    Integer minScore(@Param("testId") Integer testId);

    /** Maximum graded score. */
    @Query("select max(st.score) from StudentTest st where st.test.id = :testId and st.score is not null")
    Integer maxScore(@Param("testId") Integer testId);

    List<StudentTest> findByUserId(Integer userId);

    List<StudentTest> findByTestId(Integer testId);


}
