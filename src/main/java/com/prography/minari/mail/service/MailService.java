package com.prography.minari.mail.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
import com.prography.minari.common.service.impl.RedisProcessor;
import com.prography.minari.common.util.AuthCodeUtil;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.dto.MailVerificationReqDto;
import com.prography.minari.mail.entity.MailAuthLog;
import com.prography.minari.mail.repository.MailAuthLogRepository;
import com.prography.minari.mail.service.impl.MailTemplateCreater;
import com.prography.minari.user.dto.MailVerificationCheckReqDto;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.service.impl.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import static com.prography.minari.common.execption.ErrorCode.*;
import static com.prography.minari.mail.dto.MailSubject.AUTH_SUBJECT;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final MailTemplateCreater mailTemplateCreater;
    private final MailClient mailClient;
    private final MailAuthLogRepository mailAuthLogRepository;
    private final UserReader userReader;
    private final RedisProcessor redisProcessor;


    public void sendAuthMail(MailVerificationReqDto mailVerificationReqDto, User user) {

        String email       = mailVerificationReqDto.to();
        String redirectUri = mailVerificationReqDto.redirectUri();

        // 이미 등록된 이메일인 경우, [MAIL004] 이미 존재하는 이메일입니다.
        if(userReader.existsByEmail(email)) {
            log.info("[{}] : {}, {}", EXISTS_EMAIL.getCode(), EXISTS_EMAIL.getMessage(), email);
            throw new ApiException(EXISTS_EMAIL);
        }

        // 숫자로 구성된 6자리인증 코드 생성
        String authCode = AuthCodeUtil.generate6DigitCode();
        log.info("[메일 인증] userId: {}, 인증코드: {}", user.getId(), authCode);

        // 인증번호 레디스에 적재하기
        redisProcessor.setValue(user.getUuid(), authCode, Duration.ofMinutes(3));

        // 인증 메일 양식 만들기
        MailRequest authMailRequest = mailTemplateCreater.create(AUTH_SUBJECT.getName(),
                email,
                Map.of("verificationLink", redirectUri,"authCode",authCode),
                "auth-mail");
        // 인증 메일 전송
        mailClient.sendMail(authMailRequest);
    }

    public void verifyAuthCode(MailVerificationCheckReqDto mailVerificationCheckReqDto, User user) {

        String authCode = redisProcessor.getValue(user.getUuid())
                .orElseThrow(() -> new ApiException(EXPIRED_AUTH_CODE))
                .toString();

        // 유효햐지 않은 인증번호입니다.
        if (!authCode.equals(mailVerificationCheckReqDto.authCode())) {
            log.info("저장된 auth-code : {}, 요청한 auth-code : {}", authCode, mailVerificationCheckReqDto.authCode());
            throw new ApiException(ErrorCode.INVALID_AUTH_CODE);
        }

    }

}
