package com.prography.minari.mail.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MailVerificationReqDto(
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        String to,

        @Pattern(
                regexp = "^https?://.+",
                message = "redirectUri는 http:// 또는 https://로 시작해야 합니다."
        )
        @NotBlank(message = "redirectUri는 필수 입력 값입니다.")
        String redirectUri) {
}
