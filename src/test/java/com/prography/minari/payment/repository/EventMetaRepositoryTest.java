package com.prography.minari.payment.repository;

import com.prography.minari.payment.entity.EventMeta;
import com.prography.minari.payment.service.impl.EventTrigger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventMetaRepositoryTest {
    @Autowired
    private EventMetaRepository eventMetaRepository;

    @Test
    void 이벤트_조회_테스트() {
// given
        LocalDateTime now = LocalDateTime.of(2025, 7, 16, 12, 0);
        EventMeta validEvent = EventMeta.builder()
                .eventName("이벤트A")
                .trigger(EventTrigger.DAILY_INTERVIEW_EVENT)
                .active(true)
                .startDateTime(now.minusDays(1))
                .endDateTime(now.plusDays(1))
                .metadata("{\"productId\": 10}")
                .build();

        EventMeta expiredEvent = EventMeta.builder()
                .eventName("이벤트B")
                .trigger(EventTrigger.DAILY_INTERVIEW_EVENT)
                .active(true)
                .startDateTime(now.minusDays(5))
                .endDateTime(now.minusDays(1))
                .metadata("{\"productId\": 11}")
                .build();

        EventMeta otherTriggerEvent = EventMeta.builder()
                .eventName("이벤트C")
                .trigger(EventTrigger.USER_JOINED)
                .active(true)
                .startDateTime(now.minusDays(1))
                .endDateTime(now.plusDays(1))
                .metadata("{\"productId\": 12}")
                .build();

        eventMetaRepository.saveAll(List.of(validEvent, expiredEvent, otherTriggerEvent));

        // when
        List<EventMeta> result = eventMetaRepository.findAllByTriggerAndNowBetweenStartDateTimeAndEndDateTime(
                EventTrigger.DAILY_INTERVIEW_EVENT, now);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEventName()).isEqualTo("이벤트A");
    }
}