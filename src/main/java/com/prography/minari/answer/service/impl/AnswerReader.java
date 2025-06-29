package com.prography.minari.answer.service.impl;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.common.aop.ImplService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ImplService
@RequiredArgsConstructor
@Transactional
public class AnswerReader {
    private final AnswerRepository answerRepository;

    public List<Answer> readAllByUserIdAndQuestionId(Long userId, Long questionId) {
        return answerRepository.findAllByUserIdAndQuestionId(userId, questionId);
    }
}
