package com.prography.minari.question.entity;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {
    private FixtureMonkey fixtureMonkey;
    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }
    @Test
    void 태그조회테스트() {
        // given
        Question sample = fixtureMonkey.giveMeBuilder(Question.class)
                .set("tag", "right,left,middle,right")
                .sample();
        // when
        // then
        assertThat(sample.getTags().size()).isEqualTo(4);
    }

    @Test
    void 특정크기의태그조회테스트() {
        // given
        Question sample = fixtureMonkey.giveMeBuilder(Question.class)
                .set("tag", "right,left,middle,right")
                .sample();
        // when
        // then
        assertThat(sample.getShuffledTags(3).size()).isEqualTo(3);
    }

    @Test
    void 정해진크기보다작은경우_특정크기의태그조회테스트() {
        // given
        Question sample = fixtureMonkey.giveMeBuilder(Question.class)
                .set("tag", "middle,right")
                .sample();
        // when
        // then
        assertThat(sample.getShuffledTags(3).size()).isEqualTo(2);
    }
}