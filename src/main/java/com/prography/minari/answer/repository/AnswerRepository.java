package com.prography.minari.answer.repository;

import com.prography.minari.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select A from Answer A where A.user.id = :userId and A.question.id = :questionId order by A.createdDateTime asc")
    List<Answer> findAllByUserIdAndQuestionIdAsc(@Param("userId") Long userId, @Param("questionId") Long questionId);

    @Query(value = """
    SELECT *
    FROM (
        SELECT *, 
               ROW_NUMBER() OVER (PARTITION BY user_id, answered_date ORDER BY sequence DESC) AS rn
        FROM answers
        WHERE user_id = :userId
          AND answered_date BETWEEN :startDate AND :endDate
    ) ranked
    WHERE rn = 1
    """, nativeQuery = true)
    List<Answer> findByUserIdAndAnsweredDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT a.answeredDate) FROM Answer a WHERE a.user.id = :userId")
    Long countDistinctAnswerDateByUserId(@Param("userId") Long userId);

    long countByUserIdAndQuestionId(Long userId, Long questionId);

}
