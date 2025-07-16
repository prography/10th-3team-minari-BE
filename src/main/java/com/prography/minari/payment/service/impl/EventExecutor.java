package com.prography.minari.payment.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.payment.entity.EventMeta;
import com.prography.minari.payment.repository.EventMetaRepository;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@ImplService
@RequiredArgsConstructor
@Slf4j
public class EventExecutor {
    private final List<EventStrategy> strategies;
    private final EventMetaRepository eventMetaRepository;

    public void trigger(EventTrigger triggerType, User user) {
        LocalDateTime now = LocalDateTime.now();
        List<EventMeta> events = eventMetaRepository.findAllByTriggerAndNowBetweenStartDateTimeAndEndDateTime(triggerType, now);

        for (EventMeta event : events) {
            if(!event.isActive()){
                continue;
            }
            strategies.stream()
                    .filter(strategy -> strategy.getSupportedTrigger().equals(triggerType))
                    .filter(strategy -> strategy.getEventName().equals(event.getEventName()))
                    .findFirst()
                    .ifPresentOrElse(
                            strategy -> strategy.execute(new EventContext(user, event)),
                            () -> log.warn("전략 없음: {}({})", triggerType, event.getEventName())
                    );
        }
    }
}
