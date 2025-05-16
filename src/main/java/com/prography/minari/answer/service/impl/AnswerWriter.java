package com.prography.minari.answer.service.impl;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.common.aop.ImplService;
import lombok.RequiredArgsConstructor;

@ImplService
@RequiredArgsConstructor
public class AnswerWriter {
    private final AnswerRepository answerRepository;

    public Answer write(Answer answer) {
        return answerRepository.save(answer);
    }
}
