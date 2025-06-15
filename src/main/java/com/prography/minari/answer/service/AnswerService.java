package com.prography.minari.answer.service;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.service.dto.SttStatus;
import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.answer.service.impl.AnswerReader;
import com.prography.minari.answer.service.impl.AnswerWriter;
import com.prography.minari.answer.service.impl.AudioFileFormatConverter;
import com.prography.minari.answer.service.impl.SttProcessor;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {
    private final SttProcessor sttProcessor;
    private final AnswerWriter answerWriter;
    private final AnswerReader answerReader;
    private final UserReader userReader;
    private final QuestionReader questionReader;
    private final AudioFileFormatConverter audioFileFormatConverter;

    public void writeUserSpeech(MultipartFile file, Long userId, Long questionId, String memo) {
        User user = userReader.read(userId);
        Question question = questionReader.read(questionId)
                .orElseThrow(() -> new ApiException(ErrorCode.QUESTION_NOT_FOUND));
        List<Answer> answers = answerReader.readAllByUserIdAndQuestionId(userId, questionId);

        /**
         * Todo
         * 프론트 개발 완료후 주석 제거
         */
        /*if (!answers.isEmpty()) {
            throw new ApiException(ErrorCode.FREE_ANSWER_ALREADY_DONE);
        }*/

        byte[] inputStream = audioFileFormatConverter.convertToWavAsByte(file);
        String speech = sttProcessor.convertToText(inputStream);

        answerWriter.write(Answer.builder()
                .reply(speech)
                .user(user)
                .question(question)
                .memo(memo)
                .build());
    }

    public UserAnswerStatusResponse getSttProcessStatus(Long userId, Long questionId) {
        List<Answer> answers = answerReader.readAllByUserIdAndQuestionId(userId, questionId);
        if (answers.isEmpty()) {
            return UserAnswerStatusResponse.builder()
                    .status(SttStatus.NOT_FOUND)
                    .build();
        }
        return UserAnswerStatusResponse.builder()
                .status(SttStatus.SUCCESS)
                .build();
    }

    public InterviewContentResponse getAnswer(Long questionId, Long userId) {
        Question question = questionReader.read(questionId)
                .orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));
        List<Answer> answers = answerReader.readAllByUserIdAndQuestionId(userId, questionId);
        if (answers.isEmpty()) {
            throw new ApiException(ErrorCode.ENTITY_NOT_FOUND);
        }
        Answer answer = answers.getLast();
        return InterviewContentResponse.of(question.getAnswer(), question.getContent(), answer.getReply());
    }
}
