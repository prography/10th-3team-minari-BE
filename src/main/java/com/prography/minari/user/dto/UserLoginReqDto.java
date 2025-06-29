package com.prography.minari.user.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record UserLoginReqDto(
        @NotNull
        String code,
        @NotNull
        @URL
        String redirectUri) {
}
