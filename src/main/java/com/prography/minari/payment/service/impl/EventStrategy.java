package com.prography.minari.payment.service.impl;

public interface EventStrategy {
    EventTrigger getSupportedTrigger();
    String getEventName();
    void execute(EventContext context);
}