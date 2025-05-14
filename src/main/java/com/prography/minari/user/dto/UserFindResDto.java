package com.prography.minari.user.dto;

import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.entity.User;

public record UserFindResDto(
        Long id, String email, SocialType socialType, String socialId, String name, String image
)
{
    public static UserFindResDto from(User user) {
        return new UserFindResDto(
                user.getId(),
                user.getEmail(),
                user.getSocialType(),
                String.valueOf(user.getSocialId()), // socialId가 String이면 그대로, Long이면 변환
                user.getName(),
                user.getImage()
        );
    }
}
