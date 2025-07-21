package com.prography.minari.mail.service;

import com.prography.minari.common.execption.ApiException;
import com.prography.minari.common.execption.ErrorCode;
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

import java.time.LocalDateTime;
import java.util.Map;

import static com.prography.minari.common.execption.ErrorCode.EXISTS_EMAIL;
import static com.prography.minari.mail.dto.MailSubject.AUTH_SUBJECT;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final MailTemplateCreater mailTemplateCreater;
    private final MailClient mailClient;
    private final MailAuthLogRepository mailAuthLogRepository;
    private final UserReader userReader;

    public void sendAuthMail(MailVerificationReqDto mailVerificationReqDto, User user) {

        String email       = mailVerificationReqDto.to();
        String redirectUri = mailVerificationReqDto.redirectUri();

        // 이미 등록된 이메일인 경우, [MAIL004] 이미 존재하는 이메일입니다.
        if(userReader.existsByEmail(email)) {
            log.info("[{}] : {}", EXISTS_EMAIL.getCode(), EXISTS_EMAIL.getMessage());
            throw new ApiException(EXISTS_EMAIL);
        }

        // 숫자로 구성된 6자리인증 코드 생성
        String authCode = AuthCodeUtil.generate6DigitCode();

        log.info("[메일 인증] userId: {}, 인증코드: {}", user.getId(), authCode);

        // 인증번호 데이터베이스에 저장하기
        //TODO 차후에 Redis로 변경
        mailAuthLogRepository.save(MailAuthLog.create(user.getId(), authCode));

        // 인증 메일 양식 만들기
        MailRequest authMailRequest = mailTemplateCreater.create(AUTH_SUBJECT.getName(),
                email,
                Map.of("verificationLink", redirectUri,"authCode",authCode),
                "auth-mail");
        // 인증 메일 전송
        mailClient.sendMail(authMailRequest);
    }

    /**
     * Verifies the provided email authentication code for a user.
     *
     * Checks that the most recent authentication code for the user matches the provided code and has not expired.
     * Throws an exception if the code is missing, invalid, or expired.
     */
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
