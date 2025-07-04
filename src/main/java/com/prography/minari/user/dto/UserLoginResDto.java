package com.prography.minari.user.dto;

import com.prography.minari.common.util.JwtUtil;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.entity.User;

public record UserLoginResDto(
        Long id,
        String email,
        SocialType socialType,
        String socialId,
        String name,
        String image,
        boolean registered,
        String accessToken,
        String refreshToken)
{

    public static UserLoginResDto from(User user, String accessToken, String refreshToken) {
        return new UserLoginResDto(
                user.getId(),
                user.getEmail(),
                user.getSocialType(),
                String.valueOf(user.getSocialId()), // socialId가 String이면 그대로, Long이면 변환
                user.getName(),
                user.getImage(),
                user.isRegistered(),
                accessToken,
                refreshToken
        );
    }

}
