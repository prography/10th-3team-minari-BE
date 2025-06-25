package com.prography.minari.mail.entity;

import com.prography.minari.common.entity.BaseTimeEntity;
import com.prography.minari.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailAuthLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 인증번호(6자리 숫자)
    @Column(nullable = false, length = 6)
    private String authCode;

    // 유저 ID
    @Column(nullable = false)
    private Long userId;

    // 만료 시간 (생성 시점으로부터 10분 후)
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public static MailAuthLog create(Long userId, String authCode) {
        MailAuthLog mailAuthLog = new MailAuthLog();
        mailAuthLog.userId = userId;
        mailAuthLog.authCode = authCode;
        mailAuthLog.expiredAt = LocalDateTime.now().plusMinutes(10);
        return mailAuthLog;
    }

}
