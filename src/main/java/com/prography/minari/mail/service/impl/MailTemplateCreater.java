package com.prography.minari.mail.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.mail.dto.MailRequest;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@ImplService
@RequiredArgsConstructor
public class MailTemplateCreater {
    private final TemplateEngine templateEngine;

    public MailRequest create(String subject, String to, Map<String, String> insertValues, String htmlName) {
        Context context = new Context();
        for (Map.Entry<String, String> entry : insertValues.entrySet()) {
            context.setVariable(entry.getKey(), entry.getValue());
        }
        String autContents = templateEngine.process(htmlName, context);
        return MailRequest.create(to, subject, autContents);
    }
}
