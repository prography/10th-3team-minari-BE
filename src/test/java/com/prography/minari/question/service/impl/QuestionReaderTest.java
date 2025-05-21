package com.prography.minari.question.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuestionReader.class)
class QuestionReaderTest {
    private FixtureMonkey fixtureMonkey;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionReader questionReader;

    @AfterEach
    void init() {
        questionRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }
    @Test
    void Question엔티티_조회_테스트() {
        // given
        Question sample = fixtureMonkey.giveMeBuilder(Question.class)
                .set("id", null)
                .setNotNull("content")
                .setNotNull("answer")
                .setNotNull("tag")
                .sample();
        Question saved = questionRepository.save(sample);

        // when
        Optional<Question> optionalQuestion = questionReader.read(saved.getId());

        // then
        assertTrue(optionalQuestion.isPresent(), "저장한 질문이 존재하지 않습니다.");

        Question question = optionalQuestion.get(); // orElseThrow로 대체해도 좋음
        assertAll(
                () -> assertEquals(saved.getContent(), question.getContent()),
                () -> assertEquals(saved.getAnswer(), question.getAnswer()),
                () -> assertEquals(saved.getTag(), question.getTag())
        );
    }
}