package com.prography.minari.social.dto.enums;

import java.util.Arrays;

public enum SocialType {

    KAKAO("kakao");

    private final String value;

    SocialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SocialType from(String value) {
        return Arrays.stream(values())
                .filter(s -> s.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown social type: " + value));
    }

}
