package com.prography.minari.social.dto.social;

public record KakaoUserInfoResDto(
        Long id,
        String connected_at,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(
            Boolean profile_nickname_needs_agreement,
            Boolean profile_image_needs_agreement,
            Profile profile
    ) {}

    public record Profile(
            String nickname,
            String thumbnail_image_url,
            String profile_image_url,
            Boolean is_default_image,
            Boolean is_default_nickname
    ) {}

    public UserInfoDto to() {
        return new UserInfoDto(id, kakao_account.profile.nickname, kakao_account.profile.profile_image_url);
    }
}