package com.prography.minari.social.service.impl;

import com.prography.minari.social.dto.social.UserInfoDto;

public interface SocialClient {

    String readAccessToken(String code, String redirectUri);

    UserInfoDto readUserInfo(String accessToken);

}
