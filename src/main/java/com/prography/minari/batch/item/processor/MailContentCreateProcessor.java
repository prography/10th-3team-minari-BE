package com.prography.minari.batch.item.processor;

import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.service.impl.MailTemplateCreater;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MailContentCreateProcessor {
    private final MailTemplateCreater mailTemplateCreater;

    @Bean
    public ItemProcessor<User, MailRequest> processor() {
        return user -> {
            return mailTemplateCreater.create("오늘의 미나리",
                    user.getEmail(),
                    new HashMap<>(),
                    "today-minari");
        };
    }
}
