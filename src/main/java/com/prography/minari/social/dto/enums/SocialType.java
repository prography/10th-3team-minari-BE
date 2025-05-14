package com.prography.minari.social.dto.enums;

public enum SocialType {

    KAKAO("kakao");

    private final String value;

    SocialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
