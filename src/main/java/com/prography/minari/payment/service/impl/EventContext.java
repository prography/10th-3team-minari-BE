package com.prography.minari.payment.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prography.minari.payment.entity.EventMeta;
import com.prography.minari.user.entity.User;
import lombok.Getter;

import java.util.Map;

@Getter
public class EventContext {
    private final User user;
    private final Map<String, Object> metadata;
    private final EventMeta event;

    public EventContext(User user, EventMeta event) {
        this.user = user;
        this.event = event;
        this.metadata = parseMetadata(event.getMetadata());
    }

    private Map<String, Object> parseMetadata(String json) {
        try {
            return new ObjectMapper().readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("메타데이터 파싱 실패", e);
        }
    }

}
