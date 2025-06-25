package com.prography.minari.mail.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.util.AuthCodeUtil;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.entity.MailAuthLog;
import com.prography.minari.mail.repository.MailAuthLogRepository;
import com.prography.minari.mail.service.impl.GoogleMailClient;
import com.prography.minari.user.dto.MailVerificationCheckReqDto;
import com.prography.minari.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private static final String AUTH_SUBJECT = "[미래의 나를 위한 리워드] 이메일 인증을 완료해주세요.";

    private final GoogleMailClient googleMailClient;
    private final TemplateEngine templateEngine;
    private final MailAuthLogRepository mailAuthLogRepository;

    public void sendAuthMail(MailVerificationReqDto mailVerificationReqDto, User user) {

        // 숫자로 구성된 6자리인증 코드 생성
        String authCode = AuthCodeUtil.generate6DigitCode();

        log.info("[메일 인증] userId: {}, 인증코드: {}", user.getId(), authCode);

        // 인증번호 데이터베이스에 저장하기
        //TODO 차후에 Redis로 변경
        mailAuthLogRepository.save(MailAuthLog.create(user.getId(), authCode));

        // 인증 메일 양식 만들기
        MailRequest authMailRequest = createAuthMailTemplate(mailVerificationReqDto.to(), mailVerificationReqDto.redirectUri(), authCode);
        // 인증 메일 전송
        googleMailClient.sendMail(authMailRequest);
    }

    private MailRequest createAuthMailTemplate(String to, String redirectUri, String authCode) {
        Context context = new Context();
        context.setVariable("verificationLink", redirectUri);
        context.setVariable("authCode", authCode);
        String autContents = templateEngine.process("auth-mail", context);
        return MailRequest.create(to, AUTH_SUBJECT, autContents);
    }

    // 인증번호 검증
    public void verifyAuthCode(MailVerificationCheckReqDto mailVerificationCheckReqDto, User user) {
        MailAuthLog mailAuthLog = mailAuthLogRepository.findFirstByUserIdOrderByCreatedDateTimeDesc(user.getId())
                .orElseThrow(() -> new ApiException(ErrorCode.ENTITY_NOT_FOUND));

        // 유효햐지 않은 인증번호입니다.
        if (!mailAuthLog.getAuthCode().equals(mailVerificationCheckReqDto.authCode())) {
            throw new ApiException(ErrorCode.INVALID_AUTH_CODE);
        }

        // 인증번호가 만료되었습니다.
        if (mailAuthLog.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ApiException(ErrorCode.EXPIRED_AUTH_CODE);
        }
    }

}
