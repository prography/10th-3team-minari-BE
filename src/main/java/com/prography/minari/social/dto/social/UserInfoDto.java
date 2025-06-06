package com.prography.minari.social.dto.social;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record UserInfoDto(
        @NotNull Long socialId,
        String nickname,
        @URL String image) {
}
