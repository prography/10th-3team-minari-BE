package com.prography.minari.payment.service.impl;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.prography.minari.payment.entity.EventMeta;
import com.prography.minari.payment.repository.EventMetaRepository;
import com.prography.minari.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventExecutorTest {
    @Mock
    private EventMetaRepository eventMetaRepository;

    @Mock
    private EventStrategy sampleStrategy;

    @InjectMocks
    private EventExecutor eventService; // trigger 메서드가 포함된 클래스

    private FixtureMonkey fixtureMonkey;

    @BeforeEach
    void setUp() {
        fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();
    }

    @Test
    void 전략이_존재하면_실행된다() {
        // given
        EventTrigger triggerType = EventTrigger.USER_JOINED;
        User user = fixtureMonkey.giveMeOne(User.class);

        EventMeta eventMeta = EventMeta.builder()
                .trigger(triggerType)
                .metadata("{\n" +
                        "  \"rewardType\": \"CREDIT\",\n" +
                        "  \"amount\": 1000,\n" +
                        "  \"message\": \"신규 가입을 축하합니다! 1,000 크레딧 지급\"\n" +
                        "}")
                .eventName("WELCOME_EVENT")
                .build();

        // 전략은 trigger와 이름이 모두 일치해야 함
        given(eventMetaRepository.findAllByTriggerAndNowBetweenStartDateTimeAndEndDateTime(eq(triggerType), any()))
                .willReturn(List.of(eventMeta));

        given(sampleStrategy.getSupportedTrigger()).willReturn(EventTrigger.USER_JOINED);
        given(sampleStrategy.getEventName()).willReturn("WELCOME_EVENT");

        // 전략 리스트 주입
        eventService = new EventExecutor(List.of(sampleStrategy),eventMetaRepository);

        // when
        eventService.trigger(triggerType, user);

        // then
        verify(sampleStrategy).execute(any(EventContext.class));
    }

    @Test
    void 전략이_존재하지_않으면_실행되지_않는다() {
        // given
        EventTrigger triggerType = EventTrigger.USER_JOINED;
        User user = fixtureMonkey.giveMeOne(User.class);

        EventMeta eventMeta = EventMeta.builder()
                .trigger(triggerType)
                .eventName("WELCOME_EVENT")
                .build();

        given(eventMetaRepository.findAllByTriggerAndNowBetweenStartDateTimeAndEndDateTime(eq(triggerType), any()))
                .willReturn(List.of(eventMeta));

        // 전략은 존재하되 이름이 다름
        given(sampleStrategy.getSupportedTrigger()).willReturn(EventTrigger.USER_JOINED);
        given(sampleStrategy.getEventName()).willReturn("OTHER_EVENT");

        eventService = new EventExecutor(List.of(sampleStrategy),eventMetaRepository);

        // when
        eventService.trigger(triggerType, user);

        // then
        verify(sampleStrategy, never()).execute(any());
    }
}