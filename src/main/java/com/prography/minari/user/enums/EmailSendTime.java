package com.prography.minari.user.enums;

public enum EmailSendTime {

    AM_08("am_0800"),
    PM_12_30("pm1230"),
    PM_20("pm2000"),
    PM_22("pm2200");

    private final String value;

    EmailSendTime(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EmailSendTime from(String value) {
        for (EmailSendTime time : EmailSendTime.values()) {
            if (time.getValue().equalsIgnoreCase(value)) {
                return time;
            }
        }
        throw new IllegalArgumentException("Unknown email send time: " + value);
    }
}
