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

        boolean success = true;
        String speech = null;
        try {
            byte[] inputStream = audioFileFormatConverter.convertToWavAsByte(file);
            speech = sttProcessor.convertToText(inputStream);
        } catch (ApiException e) {
            success = false;
            throw e;
        } finally {
            // 예외가 발생하든 말든 항상 기록
            answerWriter.write(Answer.builder()
                    .reply(speech)
                    .user(user)
                    .question(question)
                    .success(success)
                    .memo(memo)
                    .build());
        }
    }

    public UserAnswerStatusResponse getSttProcessStatus(Long userId, Long questionId) {
        Optional<Answer> optionalAnswer = answerReader.readByUserIdAndQuestionId(userId, questionId);
        SttStatus sttStatus = optionalAnswer
                .map(answer -> answer.isSuccess() ? SttStatus.SUCCESS : SttStatus.ERROR)
                .orElse(SttStatus.PROCESS);
        return UserAnswerStatusResponse.builder()
                .status(sttStatus)
                .build();
    }

    public InterviewContentResponse getAnswer(Long questionId, Long userId) {
        Question question = questionReader.read(questionId)
                .orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));
        Answer answer = answerReader.readByUserIdAndQuestionId(userId, questionId)
                .orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));
        return InterviewContentResponse.of(question.getAnswer(), question.getContent(), answer.getReply());
    }
}
