package com.prography.minari.question.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ImplService
@RequiredArgsConstructor
public class QuestionReader {
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public Optional<Question> read(Long id) {
        return questionRepository.findById(id);
    }
}
