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
import java.util.List;

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

        // given
        User saveUser = userRepository.saveAndFlush(createUser());
        Question saveQuestion = questionRepository.saveAndFlush(createQuestion());

        // when
        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion, LocalDate.now()));
        Long one = answerReader.countByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());

        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion, LocalDate.now()));
        Long two = answerReader.countByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId());

        // then
        assertAll(
                () -> assertThat(one).isEqualTo(1),
                () -> assertThat(two).isEqualTo(2)
        );

    }

    @Test
    void findByUserIdAndAnsweredDateBetween_테스트() {

        // given
        User saveUser = userRepository.saveAndFlush(createUser());
        Question saveQuestion1 = questionRepository.saveAndFlush(createQuestion());
        Question saveQuestion2 = questionRepository.saveAndFlush(createQuestion());

        // when
        LocalDate today    = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion1, today));
        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion1, today));
        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion1, today));
        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion2, tomorrow));
        answerRepository.saveAndFlush(createAnswerByDate(saveUser, saveQuestion2, tomorrow));

        List<Answer> result = answerRepository.findByUserIdAndAnsweredDateBetween(saveUser.getId(), today, tomorrow);

        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.stream()
                        .filter(a -> a.getAnsweredDate().equals(today))
                        .findFirst()
                        .orElseThrow().getSequence()).isEqualTo(2L),
                () -> assertThat(result.stream()
                        .filter(a -> a.getAnsweredDate().equals(tomorrow))
                        .findFirst()
                        .orElseThrow().getSequence()).isEqualTo(1L)
        );

    }

    private User createUser() {
        return fixtureMonkey.giveMeBuilder(User.class)
                .set("id", null)
                .set("answers", new ArrayList<>())
                .setNull("seed")
                .setNull("email")
                .setNotNull("domain")
                .sample();
    }

    private Question createQuestion() {
        return fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .setNotNull("content")
                .setNotNull("answer")
                .setNotNull("tag")
                .sample();
    }

    private Answer createAnswerByDate(User saveUser, Question saveQuestion, LocalDate answeredDate) {
        return fixtureMonkey.giveMeBuilder(Answer.class)
                .set("id", null)
                .set("answeredDate", answeredDate)
                .setNotNull("reply")
                .set("user", saveUser)
                .set("question", saveQuestion)
                .set("sequence", answerRepository.countByUserIdAndQuestionId(saveUser.getId(), saveQuestion.getId()))
                .sample();
    }

}