package com.prography.minari.answer.repository;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Query("select A from Answer A where A.user.id = :userId and A.question.id = :questionId")
    Optional<Answer> findByUserIdAndQuestionId(@Param("userId") Long userId, @Param("questionId") Long questionId);

    Long user(User user);
}
