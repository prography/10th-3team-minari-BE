package com.prography.minari.batch.item.writer;

import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.service.MailClient;
import com.prography.minari.mail.service.impl.GoogleMailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailSendWriter {
    private final MailClient mailClient;

    public ItemWriter<MailRequest> send() {
        return mail -> {
            mail.iterator().forEachRemaining(mailClient::sendMail);
        };
    }
}
