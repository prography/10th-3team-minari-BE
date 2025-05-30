package com.prography.minari.user.enums;

public enum PreferredPart {

    UNSPECIFIED("파트미정"),
    FRONTEND("FrontEnd"),
    BACKEND("BackEnd"),
    FULLSTACK("FullStack");

    private final String value;

    PreferredPart(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PreferredPart from(String value) {
        for (PreferredPart part : PreferredPart.values()) {
            if (part.getValue().equalsIgnoreCase(value)) {
                return part;
            }
        }
        throw new IllegalArgumentException("Unknown preferred question part: " + value);
    }

}
