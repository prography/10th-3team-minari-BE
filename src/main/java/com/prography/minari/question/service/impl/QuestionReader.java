package com.prography.minari.question.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.entity.Domain;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.prography.minari.common.execption.ErrorCode.QUESTION_NOT_FOUND;

@ImplService
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionReader {
    private final QuestionRepository questionRepository;

    public Optional<Question> read(Long id) {
        return questionRepository.findById(id);
    }

    public Optional<Question> readDaily(User user, List<Domain> domains, long day) {
        return questionRepository
                .findDailyUnsolvedQuestionByDomains(user.getId(), domains, PageRequest.of((int) day, 1))
                .getContent().stream().findFirst();
    }

    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ApiException(QUESTION_NOT_FOUND));
    }

}
