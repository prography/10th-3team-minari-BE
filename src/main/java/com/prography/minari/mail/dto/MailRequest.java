package com.prography.minari.mail.dto;

public record MailRequest(String to, String subject, String content) {

    public static MailRequest create(String to, String subject, String content) {
        return new MailRequest(to, subject, content);
    }

}
