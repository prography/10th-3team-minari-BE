package com.prography.minari.answer.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.answer.entity.Answer;
import com.prography.minari.answer.repository.AnswerRepository;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Import(AnswerWriter.class)
class AnswerWriterTest {
    @Autowired
    private AnswerWriter answerWriter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private FixtureMonkey fixtureMonkey;

    @AfterEach
    void init() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @DisplayName("사용자의 대답 저장 구현레이어 테스트")
    @Test
    void writeTest() {
        User user = fixtureMonkey.giveMeBuilder(User.class)
                .set("id", null)
                .setNotNull("email")
                .setNotNull("domain")
                .sample();
        User saveUser = userRepository.save(user);
        Question question = fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .setNotNull("content")
                .setNotNull("answer")
                .setNotNull("tag")
                .sample();
        Question saveQuestion = questionRepository.save(question);
        Answer answer = fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion).sample();
        Answer write = answerWriter.write(answer);
        assertAll(() -> Assertions.assertEquals(answer, write));
    }
}