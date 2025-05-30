package com.prography.minari.user.enums;

public enum ExperienceLevel {

    NONE("경험 없음"),
    UNDER_1YEAR("0 ~ 1년차"),
    UNDER_3YEAR("2~3년차 미만"),
    OVER_3YEAR("3년차 이상");

    private final String value;

    ExperienceLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ExperienceLevel from(String value) {
        for (ExperienceLevel level : ExperienceLevel.values()) {
            if (level.getValue().equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown experience level: " + value);
    }

}
