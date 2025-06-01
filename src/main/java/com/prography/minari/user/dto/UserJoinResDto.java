package com.prography.minari.user.dto;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.enums.EmailSendTime;
import com.prography.minari.user.enums.ExperienceLevel;
import com.prography.minari.user.enums.PreferredPart;

public record UserJoinResDto(
        Long id,
        String email,
        Long socialId,
        String name,
        String image,
        Boolean isRegistered,
        Boolean isSubscribed,
        EmailSendTime emailSendTime,
        ExperienceLevel studyExperienceLevel,
        ExperienceLevel workExperienceLevel,
        Domain domain
) {
    public static UserJoinResDto from(User user) {
        return new UserJoinResDto(
                user.getId(),
                user.getEmail(),
                user.getSocialId(),
                user.getName(),
                user.getImage(),
                user.getIsRegistered(),
                user.getIsSubscribed(),
                user.getEmailSendTime(),
                user.getStudyExperienceLevel(),
                user.getWorkExperienceLevel(),
                user.getDomain()
        );
    }
}
