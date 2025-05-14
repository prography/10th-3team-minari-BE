package com.prography.minari.user.dto;

import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.entity.User;
import io.micrometer.common.util.StringUtils;

public record UserLoginResDto(
        Long id, String email, SocialType socialType, String socialId, String name, String image)
{
    public static UserLoginResDto from(User user) {
        return new UserLoginResDto(
                user.getId(),
                user.getEmail(),
                user.getSocialType(),
                String.valueOf(user.getSocialId()), // socialId가 String이면 그대로, Long이면 변환
                user.getName(),
                user.getImage()
        );
    }

    public boolean isNotRegistered() {
        return StringUtils.isEmpty(email);
    }
}
