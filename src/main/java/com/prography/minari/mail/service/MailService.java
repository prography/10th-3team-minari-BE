package com.prography.minari.mail.service;

import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.service.impl.MailTemplateCreater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.util.Map;

import static com.prography.minari.mail.dto.MailSubject.AUTH_SUBJECT;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailTemplateCreater mailTemplateCreater;
    private final MailClient mailClient;
    private final TemplateEngine templateEngine;

    public void sendAuthMail(MailVerificationReqDto mailVerificationReqDto) {
        // 인증 메일 양식 만들기
        MailRequest authMailRequest = mailTemplateCreater.create(AUTH_SUBJECT.getName(),
                mailVerificationReqDto.to(),
                Map.of("verificationLink", mailVerificationReqDto.redirectUri()),
                "auth-mail");
        // 인증 메일 전송
        mailClient.sendMail(authMailRequest);
    }
}
