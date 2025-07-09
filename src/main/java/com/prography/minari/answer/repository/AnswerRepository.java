package com.prography.minari.answer.repository;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select A from Answer A where A.user.id = :userId and A.question.id = :questionId")
    List<Answer> findAllByUserIdAndQuestionId(@Param("userId") Long userId, @Param("questionId") Long questionId);

    List<Answer> findByUserIdAndAnsweredDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(DISTINCT a.answeredDate) FROM Answer a WHERE a.user.id = :userId")
    Long countDistinctAnswerDateByUserId(Long userId);

    long countByUserIdAndQuestionId(Long userId, Long questionId);

}
