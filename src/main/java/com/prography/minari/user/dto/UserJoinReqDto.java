package com.prography.minari.user.dto;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import jakarta.validation.constraints.NotNull;

public record UserJoinReqDto(
        @NotNull Long userId,
        @NotNull Boolean isSubscribed,
        @NotNull EmailSendTime emailSendTime,
        @NotNull ExperienceLevel studyExperienceLevel,
        @NotNull ExperienceLevel workExperienceLevel,
        @NotNull Domain domain) {
}
