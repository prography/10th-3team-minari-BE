package com.prography.minari.answer.service;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.service.dto.response.AnswerResponse;
import com.prography.minari.answer.service.impl.AnswerWriter;
import com.prography.minari.answer.service.impl.SttProcessor;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {
    private final SttProcessor sttProcessor;
    private final AnswerWriter answerWriter;
    private final UserReader userReader;
    private final QuestionReader questionReader;

    public AnswerResponse writeUserSpeech(MultipartFile file, Long userId, Long questionId) {
        User user = userReader.read(userId);
        String speech = sttProcessor.convertToText(file);
        Question question = questionReader.read(questionId);
        answerWriter.write(Answer.builder()
                .reply(speech)
                .user(user)
                .question(question)
                .build());
        return AnswerResponse.builder()
                .answer(speech)
                .reply(question.getAnswer())
                .question(question.getContent())
                .build();
    }
}
