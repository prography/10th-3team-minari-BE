package com.prography.minari.answer.service;

import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.service.dto.SttConvertAudioFileInfo;
import com.prography.minari.answer.service.dto.SttStatus;
import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.answer.service.impl.AnswerReader;
import com.prography.minari.answer.service.impl.AnswerWriter;
import com.prography.minari.answer.service.impl.AudioFileFormatConverter;
import com.prography.minari.answer.service.impl.SttProcessor;
import com.prography.minari.aws.impl.FileUploader;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
    private final FileUploader fileUploader;
    private final AudioFileFormatConverter audioFileFormatConverter;

    /**
     * Processes a user's uploaded speech audio file for a specific question, converts it to text, saves the answer, and uploads the audio file.
     *
     * Retrieves the user and question, checks for existing answers to prevent duplicates, converts the audio to WAV format, performs speech-to-text processing, saves the resulting answer, uploads the original audio file to storage, and returns a response containing answer details.
     *
     * @param file      the uploaded audio file containing the user's speech
     * @param userId    the ID of the user submitting the answer
     * @param questionId the ID of the question being answered
     * @param memo      an optional memo to associate with the answer
     * @return an InterviewContentResponse containing the answer's details, including running time, question content, reply, and creation date
     * @throws ApiException if the question is not found or if an answer already exists for the user and question
     */
    public InterviewContentResponse writeUserSpeech(MultipartFile file, Long userId, Long questionId, String memo) {
        User user = userReader.read(userId);
        Question question = questionReader.read(questionId)
                .orElseThrow(() -> new ApiException(ErrorCode.QUESTION_NOT_FOUND));
        List<Answer> answers = answerReader.readAllByUserIdAndQuestionId(userId, questionId);

        /**
         * Todo
         * 프론트 개발 완료후 주석 제거
         */
        if (!answers.isEmpty()) {
            throw new ApiException(ErrorCode.FREE_ANSWER_ALREADY_DONE);
        }

        byte[] inputStream = audioFileFormatConverter.convertToWavAsByte(file);
        SttConvertAudioFileInfo convertResult = sttProcessor.convertToText(inputStream);

        Answer answer = answerWriter.write(Answer.builder()
                .runningTime(convertResult.getRunningTime())
                .reply(convertResult.getSpeech())
                .user(user)
                .question(question)
                .memo(memo)
                .build());
        fileUploader.upload(file, "/voice");
        return InterviewContentResponse.builder()
                .runningTime(convertResult.getRunningTime())
                .answer(question.getAnswer())
                .reply(convertResult.getSpeech())
                .question(question.getContent())
                .createDate(answer.getCreatedDateTime().toLocalDate())
                .build();

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

        return InterviewContentResponse.builder()
                .createDate(answer.getCreatedDateTime().toLocalDate())
                .runningTime(answer.getRunningTime())
                .question(question.getContent())
                .answer(question.getAnswer())
                .reply(answer.getReply())
                .build();
    }

    public List<Answer> readAnswersByDateRange(User user) {



        // 1주 범위 (월~일)
        LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 1달 범위
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        LocalDate monthEnd = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

        // 1년 범위
        LocalDate yearStart = LocalDate.now().withDayOfYear(1);
        LocalDate yearEnd = LocalDate.now().withMonth(12).withDayOfMonth(31);

        return null;

    }
}
