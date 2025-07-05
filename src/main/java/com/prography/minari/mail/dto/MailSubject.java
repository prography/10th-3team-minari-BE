package com.prography.minari.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MailSubject {
    AUTH_SUBJECT("[미래의 나를 위한 리허설] 이메일 인증을 완료해주세요.");
    private String name;
}
