package com.prography.minari.mail.service;

import com.prography.minari.mail.dto.MailRequest;

public interface MailClient {
    void sendMail(MailRequest mailRequest);
}
