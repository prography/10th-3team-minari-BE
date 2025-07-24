package com.prography.minari.user.dto;

import com.prography.minari.common.entity.Domain;
import com.prography.minari.payment.entity.Seed;
import com.prography.minari.social.dto.enums.SocialType;
import com.prography.minari.user.entity.User;
import com.prography.minari.user.enums.UserRole;

public record UserFindResDto(
        Long id,
        String email,
        SocialType socialType,
        String socialId,
        String name,
        String image,
        Long seed,
        String uuid,
        Domain domain,
        UserRole userRole,
        Long dayCount
)
{
    public static UserFindResDto from(User user, Seed seed, Long dayCount) {
        return new UserFindResDto(
                user.getId(),
                user.getEmail(),
                user.getSocialType(),
                String.valueOf(user.getSocialId()), // socialId가 String이면 그대로, Long이면 변환
                user.getName(),
                user.getImage(),
                seed.getTotal(),
                user.getUuid(),
                user.getDomain(),
                user.getUserRole(),
                dayCount
        );
    }
}
