package com.prography.minari.mail.service;

import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.service.impl.GoogleMailClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final String AUTH_SUBJECT = "[미래의 나를 위한 리워드] 이메일 인증을 완료해주세요.";

    private final GoogleMailClient googleMailClient;
    private final TemplateEngine templateEngine;

    public void sendAuthMail(MailVerificationReqDto mailVerificationReqDto) {
        // 인증 메일 양식 만들기
        MailRequest authMailRequest = createAuthMailTemplate(mailVerificationReqDto.to(), mailVerificationReqDto.redirectUri());
        // 인증 메일 전송
        googleMailClient.sendMail(authMailRequest);
    }

    private MailRequest createAuthMailTemplate(String to, String redirectUri) {
        Context context = new Context();
        context.setVariable("verificationLink", redirectUri);
        String autContents = templateEngine.process("auth-mail", context);
        return MailRequest.create(to, AUTH_SUBJECT, autContents);
    }

}
