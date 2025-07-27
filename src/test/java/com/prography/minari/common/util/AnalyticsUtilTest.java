package com.prography.minari.common.util;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.answer.dto.res.AnswerResDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AnalyticsUtilTest {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsUtilTest.class);
    private Random random = new Random();

    @Test
    void calculateConsecutiveDaysCount() {

        // given
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusDays(15);
        LocalDate endDate = now.plusDays(15);

        List<AnswerResDto> answerResDtoList = Stream
                .iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .map(date -> new AnswerResDto(random.nextBoolean(), 0L, date, 0L))
                .toList();

        Map<LocalDate, Boolean> answerExistMap = answerResDtoList.stream()
                .collect(Collectors.toMap(
                        AnswerResDto::answerDate,
                        AnswerResDto::isExisted
                ));

        int realCount = 0;
        for(LocalDate date = now; !date.isBefore(startDate); date = date.minusDays(1)) {
            if(!answerExistMap.get(date)) {
                if(date.equals(now)) continue;
                break;
            }
            realCount++;
        }

        // when
        int calculateCount = AnalyticsUtil.calculateConsecutiveDaysCount(answerResDtoList, startDate, endDate);

        // then
        assertEquals(realCount, calculateCount);

    }

}