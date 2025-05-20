package com.prography.minari.answer.service;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.answer.service.dto.SttStatus;
import com.prography.minari.answer.service.dto.UserAnswerStatusResponse;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

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
                .sample());
        answerRepository.save(fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion)
                .set("success", true)
                .sample());

        // when
        UserAnswerStatusResponse status = answerService.getSttProcessStatus(saveUser.getId(), saveQuestion.getId());

        // then
        assertAll(() -> assertEquals(SttStatus.SUCCESS, status.getStatus()));
    }

    @Test
    void 답변STT변환_상태조회_실패_테스트() {
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
                .sample());
        answerRepository.save(fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion)
                .set("success", false)
                .sample());

        // when
        UserAnswerStatusResponse status = answerService.getSttProcessStatus(saveUser.getId(), saveQuestion.getId());

        // then
        assertAll(() -> assertEquals(SttStatus.ERROR, status.getStatus()));
    }

    @Test
    void 답변STT변환_상태조회_진행중_테스트() {
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
                .sample());

        // when
        UserAnswerStatusResponse status = answerService.getSttProcessStatus(saveUser.getId(), saveQuestion.getId());

        // then
        assertAll(() -> assertEquals(SttStatus.PROCESS, status.getStatus()));
    }
}