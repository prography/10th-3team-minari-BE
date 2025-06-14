package com.prography.minari.mail.service.impl;

import com.prography.minari.common.aop.ImplService;
import com.prography.minari.common.execption.ApiException;
import com.prography.minari.mail.dto.MailRequest;
import com.prography.minari.mail.service.MailClient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

import static com.prography.minari.common.execption.ErrorCode.*;

@ImplService
@Slf4j
@RequiredArgsConstructor
public class GoogleMailClient implements MailClient {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(MailRequest mailRequest) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(mailRequest.to());                    // 메일을 받을 수신자 설정
            mimeMessageHelper.setSubject(mailRequest.subject());          // 메일의 제목 설정
            mimeMessageHelper.setText(mailRequest.content(), true); // 메일의 내용 설정
            javaMailSender.send(mimeMessage);
        } catch (AddressException e) {
            log.error("잘못된 이메일 주소입니다: {}", mailRequest.to(), e);
            throw new ApiException(EMAIL_INVALID_ADDRESS);
        } catch (MessagingException e) {
            log.error("메일 전송 중 MessagingException 발생", e);
            throw new ApiException(EMAIL_MESSAGE_CREATION_FAILED);
        } catch (MailException e) {
            log.error("Spring Mail 전송 실패", e);
            throw new ApiException(EMAIL_SEND_FAILED);
        }
    }

}
