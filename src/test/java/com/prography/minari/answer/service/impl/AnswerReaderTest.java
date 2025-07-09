package com.prography.minari.answer.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.answer.service.AnswerService;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnswerReaderTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerReader answerReader;
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
    void countDistinctAnswerDateByUserId_테스트() {

        User saveUser = userRepository.saveAndFlush(fixtureMonkey.giveMeBuilder(User.class)
                .set("id", null)
                .set("answers", new ArrayList<>())
                .setNull("seed")
                .setNull("email")
                .setNotNull("domain")
                .sample());

        Question saveQuestion = questionRepository.saveAndFlush(fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .setNotNull("content")
                .setNotNull("answer")
                .setNotNull("tag")
                .sample());

        answerRepository.saveAndFlush(fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .set("answeredDate", LocalDate.now())
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion)
                .set("sequence", 0L)
                .sample());

        Long one = answerReader.countByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());

        answerRepository.saveAndFlush(fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .set("answeredDate", LocalDate.now())
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion)
                .set("sequence", 1L)
                .sample());

        Long two = answerReader.countByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());

        assertAll(
                () -> assertThat(one).isEqualTo(1),
                () -> assertThat(two).isEqualTo(2)
        );

    }

}