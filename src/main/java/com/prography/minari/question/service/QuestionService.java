package com.prography.minari.question.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionReader questionReader;

    public String readContents(Long id) {
        Question question = questionReader.read(id).orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));
        return question.getContent();
    }
}
