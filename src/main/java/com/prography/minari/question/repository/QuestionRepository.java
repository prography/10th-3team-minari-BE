package com.prography.minari.question.repository;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.question.entity.Question;
import com.prography.minari.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
    SELECT q
    FROM Question q
    LEFT JOIN Answer a ON a.question = q AND a.user.id = :userId
    WHERE (:domains IS NULL OR q.domain IN :domains)
      AND a.id IS NULL
    ORDER BY q.orderNum ASC
    """)
    Page<Question> findDailyUnsolvedQuestionByDomains(
            @Param("userId") Long userId,
            @Param("domains") List<Domain> domains,
            Pageable pageable
    );
}
