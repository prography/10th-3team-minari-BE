package com.prography.minari.answer.service.impl;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.prography.minari.common.execption.ErrorCode.ENTITY_NOT_FOUND;

@ImplService
@RequiredArgsConstructor
@Transactional
public class AnswerReader {
    private final AnswerRepository answerRepository;

    public Optional<Answer> readByUserIdAndQuestionId(Long userId, Long questionId) {
        return answerRepository.findByUserIdAndQuestionId(userId, questionId);
    }
}
