package com.prography.minari.answer.service.impl;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.common.aop.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@ImplService
@RequiredArgsConstructor
@Transactional
public class AnswerReader {
    private final AnswerRepository answerRepository;

    public List<Answer> readAllByUserIdAndQuestionId(Long userId, Long questionId) {
        return answerRepository.findAllByUserIdAndQuestionId(userId, questionId);
    }

    public List<Answer> readAnswersByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return answerRepository.findByUserIdAndAnsweredDateBetween(userId, startDate, endDate);
    }

    public Long countByUserId(Long userId) {
        return answerRepository.countByUserId(userId);
    }

    public Long countByUserIdAndQuestionId(Long userId, Long questionId) {
        return answerRepository.countByUserIdAndQuestionId(userId, questionId);
    }
}
