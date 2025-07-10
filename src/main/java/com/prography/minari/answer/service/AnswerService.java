package com.prography.minari.answer.service;

import com.prography.minari.answer.dto.res.AnswerHistoryResDto;
import com.prography.minari.answer.dto.res.AnswerResDto;
import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.service.dto.InterviewAccessStatus;
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
import com.prography.minari.common.util.AnalyticsUtil;
import com.prography.minari.payment.service.impl.CreditCounter;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.service.impl.QuestionReader;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final CreditCounter creditCounter;

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

        // 같은 날짜에 리허설 진행 내역 존재할 경우, sequence + 1
        Long sequence = answerReader.countByUserIdAndQuestionId(userId, questionId);

        Answer answer = answerWriter.write(Answer.builder()
                .sequence(sequence)
                .runningTime(convertResult.getRunningTime())
                .reply(convertResult.getSpeech())
                .user(user)
                .question(question)
                .memo(memo)
                .answeredDate(LocalDateTime.now()
                        .minusSeconds(Math.round(convertResult.getRunningTime()))
                        .toLocalDate())
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

    public AnswerHistoryResDto getAnswerHistoryList(LocalDate startDate, LocalDate endDate, User user) {

        // answer 리스트 조회 및 날짜 기준으로 매핑
        Map<LocalDate, Answer> answerMap = answerReader.readAnswersByDateRange(user.getId(), startDate, endDate).stream()
                .collect(Collectors.toMap(
                        Answer::getAnsweredDate,
                        Function.identity()
                ));

        // startDate ~ endDate에 포함되는 Answer 데이터 전체 생성
        List<AnswerResDto> answerResList = Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .map(date -> {
                    Answer answer = answerMap.get(date);
                    return (answer != null)
                            ? AnswerResDto.createExisted(answer.getId(), date, answer.getQuestion().getId())
                            : AnswerResDto.createEmpty(date);
                })
                .collect(Collectors.toList());

        // 미나리 달성률
        int achievementRate = AnalyticsUtil.calculateAchievementRate(answerResList, startDate, endDate);

        return AnswerHistoryResDto.create(achievementRate, answerResList);
    }

    public InterviewAccessStatus determineInterviewAccess(User user) {
        Long daysSinceJoined = user.getDaysSinceJoined();

        Question question = questionReader.readDaily(user, user.getPreferDomains(), daysSinceJoined)
                .orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));

        List<Answer> answers = answerReader.readAllByUserIdAndQuestionId(user.getId(), question.getId());
        Long leftCredit = creditCounter.countNotUsedCredit(user.getId());

        // 아직 면접을 한 번도 진행하지 않은 경우
        if (answers.isEmpty()) {
            return InterviewAccessStatus.FIRST;
        }

        // 면접을 이미 봤고, 씨앗이 남아 있지 않음
        if (leftCredit == 0) {
            return InterviewAccessStatus.LIMIT_REACHED;
        }

        // 면접을 이미 봤지만, 씨앗이 있어 다시 응시 가능
        if (leftCredit > 0) {
            return InterviewAccessStatus.SEED_REQUIRED;
        }

        // 위 모든 조건에 해당하지 않을 경우
        return InterviewAccessStatus.UNKNOWN;
    }
}
