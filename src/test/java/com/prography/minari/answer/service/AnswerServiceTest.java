package com.prography.minari.answer.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.answer.dto.res.AnswerHistoryResDto;
import com.prography.minari.answer.dto.res.AnswerResDto;
import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.answer.service.dto.SttConvertAudioFileInfo;
import com.prography.minari.answer.service.dto.SttStatus;
import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.answer.service.dto.response.InterviewContentResponse;
import com.prography.minari.answer.service.impl.AudioFileFormatConverter;
import com.prography.minari.answer.service.impl.SttProcessor;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.util.AnalyticsUtil;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AnswerServiceTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerService answerService;
    @MockBean
    private SttProcessor sttProcessor;
    @MockBean
    private AudioFileFormatConverter audioFileFormatConverter;


    private FixtureMonkey fixtureMonkey;


    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .defaultNotNull(false)
                .build();
    }

    @Test
    void 답변STT변환_상태조회_성공_테스트() {
        // given
        Question saveQuestion = questionRepository.save(fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .setNotNull("content")
                .setNotNull("answer")
                .setNotNull("tag")
                .sample());
        User saveUser = userRepository.save(fixtureMonkey.giveMeBuilder(User.class)
                .set("id", null)
                .setNull("email")
                .setNotNull("domain")
                .sample());
        answerRepository.save(fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion)
                .sample());

        // when
        UserAnswerStatusResponse status = answerService.getSttProcessStatus(saveUser.getId(), saveQuestion.getId());

        // then
        assertAll(() -> assertEquals(SttStatus.SUCCESS, status.getStatus()));
    }

    @Test
    void 사용자_정답조회테스트() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .set("id", null)
                .setNull("email")
                .setNotNull("domain")
                .sample();
        User saveUser = userRepository.save(user);
        Question question = fixtureMonkey.giveMeOne(Question.class);
        Question saveQuestion = questionRepository.save(question);
        Answer answer = fixtureMonkey.giveMeBuilder(Answer.class)
                .set("question", saveQuestion)
                .set("user", saveUser)
                .setNotNull("reply")
                .sample();
        answerRepository.save(answer);

        // when
        InterviewContentResponse actual = answerService.getAnswer(saveQuestion.getId(), saveUser.getId());
        // then
        assertAll(
                () -> assertThat(actual.getReply()).isEqualTo(answer.getReply())
        );
    }

    @Test
    void STT변환후_Answer저장_테스트() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNull("email")
                .setNotNull("domain")
                .sample();
        User saveUser = userRepository.save(user);

        Question question = fixtureMonkey.giveMeOne(Question.class);
        Question saveQuestion = questionRepository.save(question);

        String memo = "이건 메모입니다.";
        String result = "테스트 응답값";

        MockMultipartFile mockFile = new MockMultipartFile(
                "audio", "audio.wav", "audio/wav", new byte[]{0, 1, 2}
        );

        when(audioFileFormatConverter.convertToWavAsByte(any()))
                .thenReturn(new byte[]{1, 1, 1});
        when(sttProcessor.convertToText(any(byte[].class)))
                .thenReturn(SttConvertAudioFileInfo.builder()
                        .speech(result)
                        .runningTime(10000D)
                        .build());

        // when
        answerService.writeUserSpeech(mockFile, saveUser.getId(), saveQuestion.getId(), memo);

        // then
        List<Answer> answerOpt = answerRepository.findAllByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());
        assertAll(
                () -> assertThat(answerOpt).isNotEmpty(),
                () -> assertThat(answerOpt.getLast().getMemo()).isEqualTo(memo),
                () -> assertThat(answerOpt.getLast().getReply()).isEqualTo(result),
                () -> assertThat(answerOpt.getLast().getUser().getId()).isEqualTo(saveUser.getId()),
                () -> assertThat(answerOpt.getLast().getQuestion().getId()).isEqualTo(saveQuestion.getId())
        );
    }

    @Test
    void STT변환API호출예외_면접결과저장안되는_테스트() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNull("email")
                .setNotNull("domain")
                .sample();
        User saveUser = userRepository.save(user);

        Question question = fixtureMonkey.giveMeOne(Question.class);
        Question saveQuestion = questionRepository.save(question);

        String memo = "이건 메모입니다.";
        String result = "테스트 응답값";

        MockMultipartFile mockFile = new MockMultipartFile(
                "audio", "audio.wav", "audio/wav", new byte[]{0, 1, 2}
        );

        when(audioFileFormatConverter.convertToWavAsByte(any()))
                .thenReturn(new byte[]{1, 1, 1});
        when(sttProcessor.convertToText(any(byte[].class)))
                .thenThrow(ApiException.class);

        // when

        // then
        assertThrows(ApiException.class, () -> answerService.writeUserSpeech(mockFile, saveUser.getId(), saveQuestion.getId(), memo));
        List<Answer> answerOpt = answerRepository.findAllByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());
        assertThat(answerOpt).isEmpty();
       /* assertAll(
                () -> assertThat(answerOpt).isNotEmpty(),
                () -> assertThat(answerOpt.getLast().getMemo()).isEqualTo(memo),
                () -> assertThat(answerOpt.getLast().getReply()).isNull(),
                ()->assertThat(answerOpt.getLast().isSuccess()).isEqualTo(false),
                () -> assertThat(answerOpt.getLast().getUser().getId()).isEqualTo(saveUser.getId()),
                () -> assertThat(answerOpt.getLast().getQuestion().getId()).isEqualTo(saveQuestion.getId())
        );*/
    }

    @Test
    void 파일형변환예외_면접결과저장안되는_테스트() {
        // given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNull("email")
                .setNotNull("domain")
                .sample();
        User saveUser = userRepository.save(user);

        Question question = fixtureMonkey.giveMeOne(Question.class);
        Question saveQuestion = questionRepository.save(question);

        String memo = "이건 메모입니다.";
        String result = "테스트 응답값";

        MockMultipartFile mockFile = new MockMultipartFile(
                "audio", "audio.wav", "audio/wav", new byte[]{0, 1, 2}
        );

        when(audioFileFormatConverter.convertToWavAsByte(any()))
                .thenThrow(ApiException.class);
        when(sttProcessor.convertToText(any(byte[].class)))
                .thenReturn(SttConvertAudioFileInfo.builder()
                        .speech(result)
                        .runningTime(10000D)
                        .build());

        // when

        // then
        assertThrows(ApiException.class, () -> answerService.writeUserSpeech(mockFile, saveUser.getId(), saveQuestion.getId(), memo));
        List<Answer> answerOpt = answerRepository.findAllByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());
        assertThat(answerOpt).isEmpty();
        /*assertAll(
                () -> assertThat(answerOpt).isNotEmpty(),
                () -> assertThat(answerOpt.getLast().getMemo()).isEqualTo(memo),
                () -> assertThat(answerOpt.getLast().getReply()).isNull(),
                ()->assertThat(answerOpt.getLast().isSuccess()).isEqualTo(false),
                () -> assertThat(answerOpt.getLast().getUser().getId()).isEqualTo(saveUser.getId()),
                () -> assertThat(answerOpt.getLast().getQuestion().getId()).isEqualTo(saveQuestion.getId())
        );*/
    }

    @Test
    void 사용자_리허설_조회_테스트() {

        User saveUser = userRepository.saveAndFlush(fixtureMonkey.giveMeBuilder(User.class)
                .setNull("email")
                .setNotNull("domain")
                .sample());

        Question saveQuestion1 = questionRepository.saveAndFlush(fixtureMonkey.giveMeOne(Question.class));
        Question saveQuestion2 = questionRepository.saveAndFlush(fixtureMonkey.giveMeOne(Question.class));
        Question saveQuestion3 = questionRepository.saveAndFlush(fixtureMonkey.giveMeOne(Question.class));

        answerRepository.saveAndFlush(Answer.builder()
                .memo("memo")
                .user(saveUser)
                .reply("reply")
                .question(saveQuestion1)
                .answeredDate(LocalDate.now().minusDays(2))
                .build());

        answerRepository.saveAndFlush(Answer.builder()
                .memo("memo")
                .user(saveUser)
                .reply("reply")
                .question(saveQuestion2)
                .answeredDate(LocalDate.now())
                .build());

        answerRepository.saveAndFlush(Answer.builder()
                .memo("memo")
                .user(saveUser)
                .reply("reply")
                .question(saveQuestion3)
                .answeredDate(LocalDate.now().plusDays(2))
                .build());

        AnswerHistoryResDto answerHistoryList = answerService.getAnswerHistoryList(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3), saveUser);

        assertAll(
                () -> assertThat(answerHistoryList.achievementRate()).isEqualTo(43),
                () -> assertThat(answerHistoryList.answerlist().stream()
                        .filter(AnswerResDto::isExisted)
                        .count()).isEqualTo(3)
        );

    }

    /**
     * Todo
     * AnswerService.class 면접 1회제한로직 해제시 함께 해제
     */
    /*@Test
    void 이미_면접완료했을때_예외발생_테스트() {
        //given
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .setNull("email")
                .setNotNull("domain")
                .sample();
        User saveUser = userRepository.save(user);

        Question question = fixtureMonkey.giveMeOne(Question.class);
        Question saveQuestion = questionRepository.save(question);

        answerRepository.save(Answer.builder()
                .memo("memo")
                .user(saveUser)
                .reply("reply")
                .question(saveQuestion)
                .build());
        MockMultipartFile mockFile = new MockMultipartFile(
                "audio", "audio.wav", "audio/wav", new byte[]{0, 1, 2}
        );
        // when
        when(sttProcessor.convertToText(any()))
                .thenReturn(SttConvertAudioFileInfo.builder()
                        .runningTime(100D)
                        .speech("speech")
                .build());
        // then
        assertThrows(ApiException.class, () -> answerService.writeUserSpeech(mockFile, saveUser.getId(), saveQuestion.getId(), null));
    }*/
}