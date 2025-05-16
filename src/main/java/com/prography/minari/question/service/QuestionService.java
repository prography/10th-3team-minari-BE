package com.prography.minari.question.service;

import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionReader questionReader;

    public String readContents(Long id) {
        Question question = questionReader.read(id);
        return question.getContent();
    }
}
