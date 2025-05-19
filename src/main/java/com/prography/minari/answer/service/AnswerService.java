package com.prography.minari.answer.service;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.service.dto.SttConvertResponse;
import com.prography.minari.answer.service.dto.SttStatus;
import com.prography.minari.answer.service.dto.response.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.answer.service.impl.AnswerReader;
import com.prography.minari.answer.service.impl.AnswerWriter;
import com.prography.minari.answer.service.impl.SttProcessor;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.prography.minari.common.execption.ErrorCode.ENTITY_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnswerService {
    private final SttProcessor sttProcessor;
    private final AnswerWriter answerWriter;
    private final AnswerReader answerReader;
    private final UserReader userReader;
    private final QuestionReader questionReader;

    public SttConvertResponse writeUserSpeech(MultipartFile file, Long userId, Long questionId) {
        User user = userReader.read(userId)
                .orElseThrow(() -> new ApiException(ENTITY_NOT_FOUND));
        Question question = questionReader.read(questionId)
                .orElseThrow(() -> new ApiException(ENTITY_NOT_FOUND));
        try {
            String speech = sttProcessor.convertToText(file);
            log.info("STT 변환 성공 - time: {}, userId: {}, questionId: {}, text: {}",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    userId,
                    questionId,
                    speech);
            answerWriter.write(Answer.builder()
                    .success(true)
                    .reply(speech)
                    .user(user)
                    .question(question)
                    .build());
            return SttConvertResponse.builder()
                    .success(true)
                    .build();
        } catch (Exception e) {
            log.warn("STT 변환 실패 - time: {}, userId: {}, questionId: {}, error: {}",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    userId,
                    questionId,
                    e.getMessage(),
                    e);
            answerWriter.write(Answer.builder()
                    .success(false)
                    .reply(null)
                    .user(user)
                    .question(question)
                    .build());
            return SttConvertResponse.builder()
                    .success(false)
                    .build();
        }
    }

    public UserAnswerStatusResponse getSttProcessStatus(Long userId, Long questionId) {
        Optional<Answer> optionalAnswer = answerReader.readByUserIdAndQuestionId(userId, questionId);
        // 답변이 아직 없다면 STT 진행 중으로 간주
        if (optionalAnswer.isEmpty()) {
            return UserAnswerStatusResponse.builder()
                    .status(SttStatus.PROCESS)
                    .build();
        }
        Answer answer = optionalAnswer.get();
        // 답변이 있고 STT 성공이면 SUCCESS
        if (answer.isSuccess()) {
            return UserAnswerStatusResponse.builder()
                    .status(SttStatus.SUCCESS)
                    .build();
        }
        // 답변이 있지만 실패 상태면 ERROR
        return UserAnswerStatusResponse.builder()
                .status(SttStatus.ERROR)
                .build();
    }
}
