package com.prography.minari.common.util;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.prography.minari.answer.entity.Answer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsUtilTest {

    private static FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
            .build();

    @Test
    void calculateConsecutiveDaysCount_0일() {

        // given
        LocalDate today = LocalDate.now();
        Map<LocalDate, Answer> localDateAnswerMap = Map.of(
                today.minusDays(2), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(3), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(4), fixtureMonkey.giveMeOne(Answer.class)
        );

        // when
        int calculateCount = AnalyticsUtil.calculateConsecutiveDaysCount(localDateAnswerMap);

        // then
        assertEquals(0, calculateCount);

    }

    @Test
    void calculateConsecutiveDaysCount_1일_오늘() {

        // given
        LocalDate today = LocalDate.now();
        Map<LocalDate, Answer> localDateAnswerMap = Map.of(
                today.minusDays(0), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(2), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(3), fixtureMonkey.giveMeOne(Answer.class)
        );

        // when
        int calculateCount = AnalyticsUtil.calculateConsecutiveDaysCount(localDateAnswerMap);

        // then
        assertEquals(1, calculateCount);

    }

    @Test
    void calculateConsecutiveDaysCount_1일_어제() {

        // given
        LocalDate today = LocalDate.now();
        Map<LocalDate, Answer> localDateAnswerMap = Map.of(
                today.minusDays(1), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(3), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(4), fixtureMonkey.giveMeOne(Answer.class)
        );

        // when
        int calculateCount = AnalyticsUtil.calculateConsecutiveDaysCount(localDateAnswerMap);

        // then
        assertEquals(1, calculateCount);

    }

    @Test
    void calculateConsecutiveDaysCount_2일_오늘() {

        // given
        LocalDate today = LocalDate.now();
        Map<LocalDate, Answer> localDateAnswerMap = Map.of(
                today.minusDays(0), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(1), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(4), fixtureMonkey.giveMeOne(Answer.class)
        );

        // when
        int calculateCount = AnalyticsUtil.calculateConsecutiveDaysCount(localDateAnswerMap);

        // then
        assertEquals(2, calculateCount);

    }

    @Test
    void calculateConsecutiveDaysCount_2일_어제() {

        // given
        LocalDate today = LocalDate.now();
        Map<LocalDate, Answer> localDateAnswerMap = Map.of(
                today.minusDays(1), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(2), fixtureMonkey.giveMeOne(Answer.class),
                today.minusDays(4), fixtureMonkey.giveMeOne(Answer.class)
        );

        // when
        int calculateCount = AnalyticsUtil.calculateConsecutiveDaysCount(localDateAnswerMap);

        // then
        assertEquals(2, calculateCount);

    }

}