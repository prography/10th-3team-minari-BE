package com.prography.minari.question.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.question.entity.Question;
import com.prography.minari.question.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Question save = questionRepository.save(sample);
        // when
        Question question = questionReader.read(save.getId())
                .orElseThrow(()->new ApiException(ErrorCode.ENTITY_NOT_FOUND));
        // then
        assertAll(()->assertEquals(save, question));

    }
}