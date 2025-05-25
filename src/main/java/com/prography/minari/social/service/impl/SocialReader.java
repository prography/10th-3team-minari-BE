package com.prography.minari.social.service.impl;

import com.prography.minari.social.dto.social.UserInfoDto;
import com.prography.minari.user.entity.User;

public interface SocialReader {

    String readAccessToken(String code, String redirectUri);

    UserInfoDto readUserInfo(String accessToken);

}
